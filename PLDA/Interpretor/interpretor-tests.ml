
(*Generic test*)
run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letf("f",["x3";"a"],M(Id("a"),Id("x3")));
  ],
  Pl( M(I(2), Pl(I(5),Id("x6")) ) ,I(-22))
));;

(* Redefining test*)
run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letf("f",["x"],Pl(I(1),Id("x")));
    Letf("f",["x"],Pl(I(2),Id("x")));
  ],
  Pl(F("f",[Id("x3")]),I(-5))
));;

(* Recursive test*)
run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Let("x4",I(4));
    Letf("f",["x"],I(2));
    Letfrec("f",["x"],If(
      (Pl(Id("x"),I(-4))),
      Id("x"),
      F("f",[Pl(Id("x"),I(3))])));
  ],
  F("f",[Id("x4")]))
);;


