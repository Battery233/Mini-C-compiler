struct a{
    int aina;
};

struct b{
    int b;
    struct a ba;
    char c;
    char array[10];
};

int i;
char c;
struct b sb;
int* iArray[10];
char cArray[10];
int* p;

int main(){
    int i;
    int j;
    char c;
    c = 'x';
    cArray[0] = 'h';
    cArray[1] = 'i';
    cArray[2] = '!';
    cArray[3] = '\0';
    i = 666;
    j = 660;
    while(j<i){
        print_i(j);
        j = j+1;
    }
    print_c(c);
    print_s((char*)cArray);
    print_s((char*)"Print!");
    if(1>2)
        print_i(i);
    else
        print_i(i+1);
//    *p = 10; Should not allow this happen in grammar?
    return -1;
    return 0;
}
