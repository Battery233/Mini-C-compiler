////////void fun(){
////////    print_s((char*)"fun");
////////}
////////struct a{
////////    int a;
////////    int b;
////////};
////////
////////struct b{
////////    int i;
////////    char c;
////////    int b;
////////    struct a sa;
////////};
//////
//////int foo(int i){
//////
//////    return i;
//////}
//////
//////void print(){
//////    print_s("VOID");
//////}
//////
//////int charfoo(char c,int j){
//////    print_s("charfoo");
//////    return j;
//////}
//////
//////int main(){
//////    int i;
//////    char x;
////////    struct a s;
////////    s.a = 66666666;
////////    s.b = 77777;
////////    print_i(s.a);
////////    foo(3,'4',5,6,7);
//////    i = foo(2);
//////    i = foo(3);
//////    print_i(i);
//////    x = charfoo('x',3);
//////    print_i(x);
//////    print();
////////    return 0;
////////}\
//////
//////int i;
//////
//////void a(){
//////    i = 3;
//////    return;
//////}
//////
////int i;
////
////void foo(){
////    char c;
////    print_i(i);
////    c = '\n';
////    print_c(c);
////    return;
////}
////
////void input(int j){
////    i = j;
////    j = 424;
////    foo();
////    print_i(j);
////}
//////int i;
////
//////int switchPlayer(int currentPlayer) {
//////  if (currentPlayer == 1) return 2;
//////  else return 1;
//////}
//////
//////int main(){
//////    i =switchPlayer(2);
//////    print_i(i);
//////    return 0;
//////}
////int main(){
//////    int i;
//////    i = read_i(i);
//////    a();
//////    print_i(i);
//////    foo();
//////    print_s((char *)"Hello World!\n");
//////    print_c('\n');
//////    print_i(131);
//////    int j;
////    int k;
////    input(555);
////    k = 134;
////    print_i(k);
////    return 12345;
////}
//struct inside{
//    int i;
//};
//
//struct a{
//    int a1;
//    char a2;
////    struct inside i;
//    int a3;
//};
//
//int main(){
//    struct a s;
//    s.a1 = 3;
//    s.a2 = 'x';
//    s.a3 = 5;
////    s.i.i = 123;
//    print_i(s.a1);
//    print_c(s.a2);
//    print_i(s.a3);
////    print_i(s.i.i);
//
//    return 0;
//}

void main(){
    char x[2];
     x[0] = 'y';
     x[1]= 'x';
    print_s((char*)x);
}