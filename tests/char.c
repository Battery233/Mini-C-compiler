#include "stdio.h"

int main(){

    char a = '';     //error here
    char b = '\nn';  //error here
    char c = 'aa';   //error here
    char e = '\3';   //error here
    char f = 'x';    //pass

    return 0;
}