//void fun(){
//    print_s((char*)"fun");
//}
//struct a{
//    int a;
//    int b;
//};
//
//struct b{
//    int i;
//    char c;
//    int b;
//    struct a sa;
//};

int foo(int i){

    return i;
}

int charfoo(char c,int j){
    print_s("charfoo");
    return j;
}

int main(){
    int i;
    char x;
//    struct a s;
//    s.a = 66666666;
//    s.b = 77777;
//    print_i(s.a);
//    foo(3,'4',5,6,7);
    i = foo(2);
    i = foo(3);
    print_i(i);
    x = charfoo('x',3);
    print_i(x);
    return 0;
}