class Main
{
 public static void main(String[] args)
 {
  System.out.println(new TestShape().test());
 }
}

class Shape
{
 int x;
 int y;

 public int print()
 {
  return 0;
 }

 public int translate (int dx, int dy)
 {
  x = x + dx;
  y = y + dy;
  return 0;
 }
}

class Rectangle extends Shape
{
 int width;
 int height;

 public int print()
 {
  System.out.println(x);
  System.out.println(y);
  System.out.println(widht);
  System.out.println(height);
  return 0;
 }
}

class Circle extends Shape
{
 int radius;

 public int print()
 {
  System.out.println(x);
  System.out.println(y);
  System.out.println(radius);
  return 0;
 }
}

class TestShape
{
 public int test()
 {
  Shape s;
  Rectangle r;
  Circle c;
  int x;

  r=new Rectangle();
  c=new Circle();

  s=r;
  s=c;
  
  x=s.print();
  x=s.translate(1,1);
  x=s.print();

  x=c.print();
  x=c.translate(2,2);
  x=c.print();

  return 0;
 }
}
