let x1=I(1);;
let x2=I(2);;
let id1=Id("id1");;
let ids=Ids.add "id1" 1 Ids.empty;;

eval id1 (Env(ids,Funcs.empty));;
run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letf("f",["x3";"a"],M(Id("a"),Id("x3")));
    Let("true",Pl(I(1331),Id("x6")));
    Let("false", M(I(111),Id("x6")))
  ],
  If (Pl( M(x2, Pl(I(5),Id("x6")) ) ,I(22)),
    Pl(F("f",[I(5);I(2)]),Id("x3")),
    Id("false"))
));;

(* Redefining test*)
run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letf("f",["x"],Pl(I(1),Id("x")));
    Letf("f",["x"],Pl(I(2),Id("x")));
    Let("true",Pl(I(1331),Id("x6")));
    Let("false", M(I(111),Id("x6")))
  ],
  F("f",[Id("x3")])
));;

(* Recursive test*)
run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letfrec("f",["x"],If( 
      (Pl(Id("x"),I(-4))),
      Id("x"),
      F("f",[Pl(Id("x"),I(2))])));
    Let("true",Pl(I(1331),Id("x6")));
    Let("false", M(I(111),Id("x6")))
  ],
  F("f",[I(4)])
));;

