type lambda= E of string
	    |L of string * lambda
	    |Ap of lambda * lambda;;

module SS=Set.Make(String);;

let rec l_to_s l=match l with E(name)-> name
  |L(name,v) -> "L("^name^","^(l_to_s v)^")"
  |Ap(e1,e2) -> (l_to_s e1)^" "^(l_to_s e2);;

let rec ss_to_s names=SS.fold (fun a b->a^b) names "";;

let rec namesOf ex names=match ex with
    E(a)->SS.add a names
  |L(a,e)->namesOf e (SS.add a names)
  |Ap(a,b)->namesOf a (namesOf b names);;

let has var set=SS.exists (fun a ->a=var) set;;

let rec rename dest names=if SS.is_empty names 
  then dest 
  else ((*Printf.printf "renaming %s for %s \n" (l_to_s dest) (ss_to_s names); *)
	match dest with
	  E(a) -> if (has a names) then E("."^a) else dest
	|L(a,e) -> if (has a names) then L("."^a,rename e names) else L(a,rename e names)
	|Ap(e1,e2) ->Ap(rename e1 names,rename e2 names));;

let rec substitute ex1 rname ex2=
(* Printf.printf "subst   %s in %s with %s\n" rname (l_to_s ex1) (l_to_s ex2);*)
  match ex1 with
    E(name)-> if(name=rname) then ex2 else ex1
  |L(name,exp)-> if(name<>rname) 
    then L(name,substitute exp rname ex2) 
    else (substitute exp rname ex2)
  |Ap(exA,exB)-> Ap((substitute exA rname ex2),(substitute exB rname ex2));;

let rec eval lambd=
  Printf.printf "eval    %s \n" (l_to_s lambd);
  match lambd with 
      E(value)->E(value);
    |L(param,value)->L(param,value);
    |Ap(ex1,ex2)->match ex1 with
      E(_) -> Ap (ex1,ex2)
      |L(name,exDest) ->eval (substitute (rename exDest (SS.inter (namesOf exDest SS.empty) (namesOf ex2 SS.empty))) name ex2) 
      |Ap(exA,exB) -> eval (Ap((eval ex1),ex2));;

let run lambd=eval lambd;;

(*  ^y.^x.x a b -> b *)
let x=Ap(Ap(L("y",L("x",E("x"))),E("x")),E("b"));;
run(x);;

(*  ^y.^x.x a -> ^x.x *)
run(Ap(L("y",L("x",E("x"))),E("x")));;

(*  ^y.^x.(y x) ^x.x b -> b *)
let y=Ap(Ap(L("y",L("x",Ap(E("y"),E("x")))),L("x",E("x"))),E("b"));;
run(y);;

