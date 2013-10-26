(* Expr type.*)
type expr= I of int
  |Id of string
  |Pl of expr*expr (* + *)
  |M of expr*expr  (* * *)
  |If of expr*expr*expr
  |F of string*expr list ;; (* id id* *)

(* Def type *)
type def = Let of string*expr
  |Letf of string*string list *expr
  |Letfrec of string*string list*expr;;

(* -> int *)
module Ids=Map.Make(String);;
(* -> Body'*)
module Funcs=Map.Make(String);;

(* Defined ids and function at one moment. *)
type 'b env=Env of int Ids.t * 'b Funcs.t ;;

(* Custom type for keeping function parameter list and body. *)
type body =Body of string list *expr* body env * bool;;

(* Prog type*)
type prog = P of def list * expr;;

(*debug flag*)
let debug=true;;

(* Environment debug function. *)
let printEnv env tag=match env with Env(ids,funcs)->
  Printf.printf("env from %s:\n") tag; 
  Ids.iter (fun key value->Printf.printf ("Id:%s %d\n") key value) ids;
  Funcs.iter (fun key value->Printf.printf ("F:%s\n") key) funcs;;

(*
args -> remaining arguments with which the function is called
body -> (remaining arguments name,function body) for the function
env -> (ids value that may include arguments that were evaluated,functions)
*)
let rec evalFunc args body =(
  match args with 
    hda::tla ->(
      match body with 
	Body(hd::tl,ex,env1,isRec)->
	  (match env1 with Env(ids1,funcs1) ->
	    evalFunc tla 
	      (Body(tl,ex,(Env((Ids.add hd hda ids1),funcs1)),isRec))
	  )
      (* evaluate the function with one less argument *)
      |Body([],ex,env1,_)->eval ex env1
    )
  (* all arguments have been evaluated so call function *)
  |[] -> (match body with Body(_,ex,env1,_)-> (eval ex env1))
)
(*
Intermediar step to add recursive functions to their own environment.
name-> of the function
args-> evaluated arguments of the function
body-> of the function
*)
and preEvalFunc name args body =(match body with Body(params,ex,env,isRec)->
  if isRec
    then match env with Env(ids,funcs)-> 
      (evalFunc args (Body(params,ex,Env(ids,Funcs.add name body funcs),isRec)))
    else (evalFunc args body)
)
(*
Evaluates the function call arguments.
This must be done in the enviroment in which the function is called.
arg-> arguments to be evaluated
env-> environment
evaluations-> (stores the temporary result, the function should be called with [])
*)
and evalArgs args env evaluations=(match args with
    hd::tl-> ((eval hd env)::(evalArgs tl env evaluations))
  |[]-> []
)
(* expression evaluation
x ->expresion to evaluate
env-> (ids mix of local and global ids,functions)
*)
and eval x env=match x with I(a)->a
  |Id(id)->(*printEnv env "before id eval";print_string("\neval id:");print_string(id);*)
     (match env with Env(ids,funcs) ->Ids.find id ids )
  |Pl(a,b)-> (eval a env) + (eval b env)
  |M(a,b)-> (eval a env) * (eval b env)
  |If(a,b,c) -> if (eval a env)!=0 then (eval b env) else (eval c env)
      (* find the function body and evaluate*)
  |F(name,args)->(*printEnv env "Before eval func";*)
    (match env with Env(ids,funcs)->(
      preEvalFunc name (evalArgs args env []) (Funcs.find name funcs))
    );;

(* Define ids and functions
def' d -> id or function to define
env -> (ids,functions) that were defined
*)
let rec defin d env=match env with Env(ids,funcs)->(
  (*printEnv env "Before definition:";*)
  match d with
    Let(name,value) ->(*print_string("\ndefin:");print_string(name);*)
      Env(Ids.add name (eval value env) ids,funcs)
  |Letf(f,args,ex) -> 
    Env(ids,Funcs.add f (Body(args,ex,env,false)) funcs) 
  |Letfrec(f,args,ex)->
    Env(ids,Funcs.add f (Body(args,ex,env,true)) funcs)
);;

(* Evaluate the program *)
let rec evalProg p env=match p with
    P([],ex) -> eval ex env
      (* add a new definition and evaluate the program *)
  |P(hd::tl,ex) -> evalProg (P (tl,ex)) (defin hd env);;

let run p=match p with P(d,e) -> evalProg p (Env(Ids.empty,Funcs.empty));;

