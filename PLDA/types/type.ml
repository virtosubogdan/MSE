type expr= Num
	    |Var of string
	    |Add of expr * expr
	    |If of expr * expr * expr
	    |Fun of string * expr
	    |Expr of expr * expr;;
type tip=Int|Arr of tip*tip|V of string;;

let debug=false;;
let new_count =
 let r = ref 0 in
 let next () = r := !r+1; !r in
 next;;
(* New variable name *)
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
  |V(a) ->a;; 
let pt a=Printf.printf "Tip: %s\n" (printtip a);;
let rec expand m=function
  |Int -> Int
  |Arr(a,b)->Arr(expand m a,expand m b)
  |V(a)->if has m a then expand m (S.find a m) else V(a);; 
let pm m=Printf.printf "Map:\n";S.iter (fun a b->Printf.printf "%s : %s\n" a (shp b) ) m;;


(*unify 2 types*)
let rec unify m =function
  |(Arr(a,a1),Arr(b,b1))-> if debug then Printf.printf "unify ar %s to ar %s\n" (shp (Arr(a,a1))) (shp (Arr(b,b1)));
      (match unify m (a,b) with m1 -> unify m1 (a1,b1))
  |(V(numea),V(numeb))->if numea=numeb then m else S.add numea (V(numeb)) m
  |(V(a),tip)->if debug then Printf.printf "unify %s to type %s\n" a (shp tip); S.add a tip m
  |(Int,Int)->m;
  |(Arr(a,a1),V(b))->unify m (V(b),Arr(a,a1))
  |(Arr(a,a1),Int)->failwith "cannot match function with Int";
  |(Int,V(b))-> unify m (V(b),Int);
  |(Int,Arr(a,a1))->failwith "cannot match Int to function";; 
(*unify 2 types with debug*)
let unif m=function 
  |(a,b) ->if debug then Printf.printf "unif %s with %s\n" (shp (expand m a)) (shp (expand m b));
      unify m (expand m a,expand m b);;
(*returns the type of an expression
m-> type name to type map. Contains the types already defined
*-> expression
*)
let rec tipOf m =function 
  | If (a,b,c)-> (match tipOf m a with (ma,ta)->
    match unify ma (ta,Int) with mma->
      match tipOf mma b with (mb,tb)->
	match tipOf mb c with (mc,tc)->
	  (unify mc (tb,tc),tc) )
  |Num -> (m,Int)
  |Var(nume) -> 
     if has m nume 
    then (m,S.find nume m) 
    else failwith ("unbound value "^nume)
  |Fun(nume,body) -> (match tipOf (S.add nume (V(nv())) m) body with (mres,tip) ->
			if debug then Printf.printf "fun %s \n" nume;if debug then pt (mres,tip);
			(mres,Arr(S.find nume mres,tip))
		     )
  |Expr(a,b)->(match tipOf m a with (m1,tipa) ->
    (match (tipOf m1 b,nv()) with ((m2,tipb),nv) -> 
		   if debug then Printf.printf "Expr %s to %s \n" (shp tipa) (shp tipb);
		   (unif m2 ( tipa ,Arr(tipb,V(nv))),V(nv))
		 )
  )
  |Add(a,b)->(match tipOf m a with (ma,ta)->
    match tipOf m b with (mb,tb)->
      (unify (unify mb (ta,Int)) (tb,Int),Int));;
	     
let x3=Fun("x3",Var("x3"));;
fun x2->fun x3-> x2 (x2 x3);;
let x2=Fun("x2",Fun("x3",Expr(Var("x2"),Expr(Var("x2"),Var("x3")))));;
fun x1->fun x2->fun x3 -> x1 (x1 x2) (x1 x2 x3);; 
let x1=Fun("x1",Fun("x2",Fun("x3",
			     Expr(Expr(Var("x1"),Expr(Var("x1"),Var("x2")))
			       ,Expr(Expr(Var("x1"),Var("x2")),Var("x3"))
			     ))));;
fun x0 -> fun x1 -> fun x2 -> fun x3 -> 
  (
    (x0 (x0 x1)) 
      ((x0 x1) x2))
    (((x0 x1) x2) 
	x3
    );;
let x0=Fun("x0",Fun("x1",Fun("x2",Fun("x3",Expr
  (Expr
     (Expr(Var("x0"),Expr(Var("x0"),Var("x1")))
	,Expr(Expr(Var("x0"),Var("x1")),Var("x2"))
     ),
   Expr
     (Expr(Expr(Var("x0"),Var("x1")),Var("x2"))
	,Var("x3")
     )
  )
))));;
let tipIf=Fun("x0",If(Var("x0"),Var("x0"),Var("x0")));;
let tipAdd=Fun("x0",Add(Var("x0"),Var("x0")));;
printtip (tipOf S.empty x0);;
      
