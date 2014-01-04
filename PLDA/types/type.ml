
type expr= Num
	    |Var of string
	    |Add of expr * expr
	    |If of expr * expr * expr
	    |Fun of string * expr
	    |Expr of expr * expr;;
type tip=Int|Arr of tip*tip|V of string;;




(*
let new_count =
 let r = ref 0 in
 let next () = r := !r+1; !r in
 next;;

let nv=fun ()->Printf.sprintf "%d" (new_count());; 
module S=Map.Make(String);;

let has m a= S.fold (fun z x y->(z=a)||y) m false;;

let rec printtip = function 
  |(m,Int) -> "int"
  |(m,Arr(a,b)) -> "("^(printtip (m,a))^") -> "^(printtip (m,b))
  |(m,V(a)) -> if has m a then printtip (m,(S.find a m)) else a;; 

let rec shp=function
  |Int -> "int"
  |Arr(a,b) -> "("^(shp a)^") -> "^(shp b)
  |V(a) ->Printf.printf "x";a;; 

let pt a=Printf.printf "Tip: %s\n" (printtip a);;

let rec expand m=function
  |Int -> Int
  |Arr(a,b)->Arr(expand m a,expand m b)
  |V(a)->if has m a then expand m (S.find a m) else V(a);; 

let pm m=Printf.printf "Map:\n";S.iter (fun a b->Printf.printf "%s : %s\n" a (shp b) ) m;;

let rec unify m =function
  |(Arr(a,a1),Arr(b,b1))-> Printf.printf "unify ar %s to ar %s\n" (shp (Arr(a,a1))) (shp (Arr(b,b1)));
      (match unify m (a,b) with m1 -> unify m1 (a1,b1))
  |(V(a),tip)->Printf.printf "unify %s to type %s\n" a (shp tip);S.add a tip m
  |(Int,Int)->m;
  |(Arr(a,a1),V(b))->unify m (V(b),Arr(a,a1))
  |(Arr(a,a1),Int)->failwith "cannot match function with Int";
  |(Int,V(b))-> unify m (V(b),Int);
  |(Int,Arr(a,a1))->failwith "cannot match Int to function";; 


(*

user chooses if to see next solution.
sigma is a map;
*)


let unif m=function 
  |(a,b) ->Printf.printf "unif %s with %s\n" (shp (expand m a)) (shp (expand m b));
      unify m (expand m a,expand m b);;

let rec tipOf m =function 
  | If (a,b,c)-> tipOf m a
  |Num -> (m,Int)
  |Var(nume) -> 
     if has m nume 
    then (m,S.find nume m) 
    else failwith ("unbound value "^nume)
  |Fun(nume,body) -> Printf.printf "f";(match tipOf (S.add nume (V(nv())) m) body with (mres,tip) ->
			Printf.printf "fun %s \n" nume; pt (mres,tip);
			(mres,Arr(S.find nume mres,tip))
		     )
  |Expr(a,b)->Printf.printf "e";(match tipOf m a with (m1,tipa) ->
		 (match (tipOf m1 b,nv()) with ((m2,tipb),nv) -> 
		    Printf.printf "Expr %s to %s \n" (shp tipa) (shp tipb);
		    (unif m2 ( tipa ,Arr(tipb,V(nv))),V(nv))
		 )
	      );;
	       (* _ -> "?";;*)


printtip (tipOf S.empty (Fun ("x0",Fun("x1",Expr(Var("x0"),Expr(Var("x0"),Var("x1")))))));;

fun x0 -> fun x1 -> x0 (x0 x1);;
		   

	    let x=Fun("x0",
		 Fun("x1",
		     Fun("x2",
			 Fun("x3",
			     Expr(Expr(Expr(Var("x0"),
			       Expr(Var("x0"),Var("x1"))),
				       Expr(Expr(Var("x0"),Var("x1")),Var("x2"))),
				  Expr(Expr(Expr(Var("x0"),Var("x1")),Var("x2")),Var("x3")))))));;


fun x0 -> fun x1 -> fun x2 -> fun x3 -> x0 (x0 x1) (x0 x1 x2) (x0 x1 x2 x3);;
*)


open Scanf

type expr = Var of string        (* only lambda calculus, no ints *)
            | Fun of string * expr
            | Call of expr * expr

let skipspace() = scanf "%_[\t\011\012\r ]" ()

let rec term outer =         (* fun ... -> ... needs () unless outer *)
  skipspace();
  try scanf "(" ();
      let r = expr() in (
        skipspace();
        try scanf ")" (); r
        with End_of_file | Scan_failure _ -> failwith "missing )")
  with Scan_failure _ -> scanf "%[0-9A-Za-z_]" (fun s ->
    if s = "" then failwith "term expected"
    else if s <> "fun" then Var s
    else if outer then (skipspace (); 
                        scanf "%[0-9A-Za-z_]" (fun s ->
                          skipspace(); scanf "->" (); 
                          Fun (s, expr())))
    else failwith "need () around fun _ -> ...")
and expr() =
  let rec restexpr t =
    skipspace();
    scanf "%0c" (function 
  '\n' | ')' -> t 
    |   _ -> restexpr (Call (t, term false)))
  in restexpr (term true)

let readexpr () = let e = expr() in scanf "\n" (); e

