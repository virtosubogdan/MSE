
let test x= if x==1 then print_string("passed") else print_string("failed");;

(*Generic test*)
test (run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letf("f",["x3";"a"],M(Id("a"),Id("x3")));
  ],
  If (Pl( M(I(2), Pl(I(5),Id("x6")) ) ,I(-22)),
    I(0),
    I(1))
)));;

(* Redefining test*)
test (run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letf("f",["x"],Pl(I(1),Id("x")));
    Letf("f",["x"],Pl(I(2),Id("x")));
  ],
  If((Pl(F("f",[Id("x3")]),I(-5))),
     I(0),
     I(1))
)));;

(* Recursive test*)
test (run(P(
  [
    Let("x3",Pl(I(2),I(1)));
    Let("x6",I(6));
    Letfrec("f",["x"],If( 
      (Pl(Id("x"),I(-4))),
      Id("x"),
      F("f",[Pl(Id("x"),I(2))])));
  ],
  Pl(F("f",[I(4)]),I(-6))
)));;

