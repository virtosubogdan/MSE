type lambda= E of string
	    |L of string * lambda
	    |Ap of lambda * lambda;;

module SS=Set.Make(String);;

let rec l_to_s l=match l with E(name)-> name
  |L(name,v) -> "L("^name^","^(l_to_s v)^")"
  |Ap(e1,e2) -> (l_to_s e1)^" "^(l_to_s e2);;

let rec ss_to_s names=SS.fold (fun a b->a^b) names "";;

(* Return in <names> all the names of bound variables in the expression <ex>. *)
let rec boundNamesOf ex names=match ex with
    E(a)->names
  |L(a,e)->boundNamesOf e (SS.add a names)
  |Ap(a,b)->boundNamesOf a (boundNamesOf b names);;

(* Return in <names> all the names of variables in the expression <ex>. *)
let rec namesOf ex names=match ex with
    E(a)->(SS.add a names)
  |L(a,e)->namesOf e (SS.add a names)
  |Ap(a,b)->namesOf a (namesOf b names);;

(* Return true is the string set <set> contains the string <var>. *)
let has var set=SS.exists (fun a ->a=var) set;;

(* Rename all the values in the expression <dest>, values with names contained in 
the set <names> or ==except, with "_"^variable name. *)
let rec rename dest names except=
  (* Printf.printf "renaming %s for %s and %s\n" (l_to_s dest) (ss_to_s names) except; *)
  match dest with
      E(a) -> if (has a names||a==except) then E("_"^a) else dest
	|L(a,e) -> if (has a names||a==except) then L("_"^a,rename e names except) else L(a,rename e names except)
	|Ap(e1,e2) ->Ap(rename e1 names except,rename e2 names except);;

(* Substitute all the variables with the name <rname> in <ex1> with <ex2>. *)
let rec substitute ex1 rname ex2=
  (* Printf.printf "subst   %s in %s with %s\n" rname (l_to_s ex1) (l_to_s ex2);*)
  match ex1 with
      E(name)->((*print_string(name);*) if(name=rname) then ex2 else  ex1)
  |L(name,exp)-> L(name,substitute exp rname ex2) 
  |Ap(exA,exB)-> Ap((substitute exA rname ex2),(substitute exB rname ex2));;

(* Evaluate the expression <lambd>. *)
let rec eval lambd=
  Printf.printf "eval    %s \n" (l_to_s lambd);
  match lambd with 
      E(value)->E(value);
    |L(param,value)->L(param,value);
    |Ap(ex1,ex2)->match ex1 with
	  E(_) -> Ap (ex1,ex2)
	|L(name,exDest) ->
	   eval (substitute (rename exDest(SS.inter (boundNamesOf exDest SS.empty)(namesOf ex2 SS.empty)) name) name ex2) 
	|Ap(exA,exB) -> eval (Ap((eval ex1),ex2));;

(*  ^y.^x.(x y) x b -> b *)
let x=Ap(Ap(L("y",L("x",Ap(E("x"),E("y")))),E("x")),E("b"));;
(* eval(x);;*)

(*  ^x.^y.(x y) ^y.y a ->  *)
let x1=Ap(Ap(L("x",L("y",Ap(E("x"),E("y")))),L("y",E("y"))),E("a"));;
(* eval(x1);; *)

(* ^x.(x x) ^x.(x x) <-> *)
let paradox=Ap(L("x",Ap(E("x"),E("x"))),L("x",Ap(E("x"),E("x"))));;
(*eval(paradox);;*)

