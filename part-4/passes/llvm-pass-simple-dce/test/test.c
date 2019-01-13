int foo(){
  int i = 3;
  int a = 2; //dead
  i +=1;
  int c = i-a; //dead
  return i;

}

void foo2(){
  int a = 3; //dead
  int b = a+2; //dead
}

int main() {
  int a = foo();
  int b = a * 2;
  int c = b - a;   // dead 
  int d = c / a;   // dead
  return b;
}

