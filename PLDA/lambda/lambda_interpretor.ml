
type e= V of string
	|Def of string * e
	|App of e * e;;

module Env =Map.Make(String);; 

let rec ev ex env app=match ex with 
    V(name)->if(Env.mem name env) then Env.find name env else ex
  |Def(name,ex1)-> 
     (match app with 
	  hd::tl->(ev ex1 (Env.add name hd env) tl)
	|[] ->V("err"))
  |App(ex1,ex2)->ev ex1 env (ex2::app);;


let eval ex=ev ex Env.empty [];;

(* ^p.^q.p q p *)
eval(App(App(Def("p",Def("q",V("p"))),V("q")),V("p")));; 
