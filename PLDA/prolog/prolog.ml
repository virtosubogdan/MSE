open Scanf
type term = Var of string 
	    | Fun of string * term list;;
module SM=Map.Make(String);;

let rec to_string=function Var(name)->name
  |Fun(name,list)-> name^"("^(printTerm list)^")"
and  printTerm =function h::tl-> (to_string h)^","^(printTerm tl)
  |[] ->"";;
let print x=Printf.printf "%s\n" (printTerm x);;
(* for debug *)
let d=false;;
let printmap x=
  let rec value map k v=match v with 
      Var(name)-> value map name (SM.find name map)
    |Fun(name,_)-> name
  in
  (if SM.is_empty x then Printf.printf "true"
   else SM.iter 
      (fun k v-> if String.get k 0 ='.' 
	then Printf.printf "" 
	else Printf.printf "%s->%s\n" k (value x k v)) 
      x);
  Printf.printf "\n";;
let rec merge a b=match a with h::tl -> h::(merge tl b)|[]->b;;
let rec has a map=SM.fold (fun z x y->(z=a)||y) map false;;
let pTrue=Fun("true",[]);;
let userEx="Not showing other solutions.\n";;
let assertGoNext=fun ()->
  Printf.printf "Type ; for next solution\n";flush stdout;
  scanf "%c\n" (fun x->if x<>';' then failwith userEx);;

(* adds a depth appropriate prefix *)
let rec n c str=if c=0 then "."^str else n (c-1) ("."^str)
(* rename all variables according to depth *)
and rename c=function
    h::tl->(match h with Var(name)->Var(n c name)
	      |Fun(name,term)->Fun(name,rename c term))::(rename c tl)
  |_->[]
(* unify a list of terms *)
and unifele map=function 
    (ha::ta,hb::tb)->unifele (unify ha hb map) (ta,tb)
  | ([],[])->map
  | _->failwith "unifele:different numbers";
(* unify 2 terms*)
and unify a b map=if d then Printf.printf "%s\n" ("unify "^(to_string a)^" "^(to_string b));
  match (a,b) with
      (Fun(numea,arga),Fun(numeb,argb))->if numea=numeb then 
	unifele map (arga,argb)
      else failwith ("unify:different names "^numea^" "^numeb)
    |(Var(numev),Fun(numef,[]))
    |(Fun(numef,[]),Var(numev))-> if has numev map
      then unify (Fun(numef,[])) (SM.find numev map) map
      else SM.add numev (Fun(numef,[])) map
    |(Var(numea),Var(numeb))->if has numea map 
      then unify (SM.find numea map) b map
      else SM.add numea (Var(numeb)) map 
	(*(if has numeb map 
	  then unify a (SM.find numeb map) map
	  else SM.add numea (Var(numeb)) map)*)
    |_->failwith ("cannot unify "^(to_string a)^" with "^(to_string b) )
(* try/fail approach of expanding a predicate
c->depth of recursion
kb->all the knowledge base
rest->the rest of the predicates that still need to be matched after
pred->predicate that is now matched
map->already mapped variables
*->remaining rules in the knowledge base that have to be checked for a match  
*)
and incearca c kB rest pred map=function
    (rule,expand)::t->
      (match rename c [rule] with [renamedRule] -> 
	 (try eval (c+1) kB (merge (rename c expand) rest) (unify pred renamedRule map) 
	  with Failure(esec)-> 
	    if esec=userEx
	    then failwith esec
	    else if d then Printf.printf "incearca fail:%s\n" esec)
	 |_ ->failwith "imposible");
      incearca c kB rest pred map t 
  |[]->failwith ("no match in kb for "^(to_string pred))
(* attempt to evaluate all the solutions for a list of rules
c->depth of the recursion
kb->all the knowledge base
list->predicates that have to be matched
map->already mapped variables
*)
and eval c kb list map=match list with 
    h::t-> (try (if h=pTrue 
		 then eval c kb t map 
      else incearca c kb t h map kb)  with Failure(reason) -> 
	      if reason=userEx 
	      then failwith reason 
	      else if d then Printf.printf "%s\n" reason) 
  |[]->printmap map; assertGoNext();;


eval 0 [ (Fun("parinte",[Fun("tataion",[]) ;Fun("ion",[])] ),[]);
        (Fun("parinte",[Fun("alina",[]) ;Fun("andrei",[])] ),[]);
	(Fun("parinte",[Fun("ion",[]) ;Fun("andrei",[])] ),[]);
	(Fun("parinte",[Fun("andrei",[]);Fun("fiuAndrei",[])]),[]);
	(Fun("descendent",[Var("x");Var("y")]),
	 [Fun("parinte",[Var("x");Var("y")])]);
	(Fun("descendent",[Var("x");Var("y")]),
	 [Fun("parinte",[Var("x");Var("z")]);Fun("descendent",[Var("z");Var("y")])])
     ]
  [Fun("descendent",[Fun("tataion",[]);Var("y")])]
 SM.empty;;



