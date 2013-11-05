(* basic working draft *)

(* no operations, just substitution *)
(* problem: same names, solution string set*)

type lambda= E of string
	    |L of string * lambda
	    |Ap of lambda * lambda;;

let rec l_to_s l=match l with E(name)-> name
  |L(name,v) -> "L("^name^","^(l_to_s v)^")"
  |Ap(e1,e2) -> (l_to_s e1)^" "^(l_to_s e2);;

module SS=Set.Make(String);;

let rec substitute ex1 rname ex2=
  Printf.printf "subst   %s in %s with %s \n" rname (l_to_s ex1) (l_to_s ex2);
  match ex1 with
    E(name)-> if(name=rname) then ex2 else ex1
  |L(name,exp)-> if(name<>rname) then ex1 else (substitute exp rname ex2)
  |Ap(exA,exB)-> Ap((substitute exA rname ex2),(substitute exB rname ex2));;

let rec replace ex1 ex2=
  Printf.printf "replace %s with %s \n" (l_to_s ex1) (l_to_s ex2);
  match ex1 with
    E(_) -> ex1
  |L(name,exDest) -> substitute exDest name ex2
  |Ap(exA,exB) -> Ap((eval ex1),ex2);

and eval lambd =
  Printf.printf "eval    %s \n" (l_to_s lambd);
  match lambd with 
      E(value)->E(value);
    |L(param,value)->L(param,value);
    |Ap(ex1,ex2)->eval (replace ex1 ex2);;

let run lambd=eval lambd;;

(*  ^y.^x.x a b -> b *)
run(Ap(Ap(L("y",L("x",E("x"))),E("a")),E("b")));;

(*  ^y.^x.x a -> ^x.x *)
run(Ap(L("y",L("x",E("x"))),E("a")));;
