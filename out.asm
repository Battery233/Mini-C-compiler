.data
i: .space 4
c: .space 4
sb: .space 24
iArray: .space 40
cArray: .space 12
p: .space 4
######end of declaration######
.text
jal main
######declaration of functions######

main:
move  $fp, $sp
###visit block###
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $t9, 666
sw $t9, 4($fp)
lw $t9, 4($fp)
# print_i
li $v0, 1
add $a0, $zero, $t9
syscall
# print_i

li $t9, 0
add   $v0, $zero, $t9
###visit block end###
