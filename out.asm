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
###block vd↑###block stmt↓###
li $t9, 'h'
la $s6,cArray
li $s6, 0
sw $t9, ($t8), # visitAssign Tag 6
li $t9, 0
li $t8, 1
sub $s5, $t9, $t8
add   $v0, $zero, $s5
move $a0, $v0
li $v0, 17, 
syscall
jr $ra
li $s5, 0
add   $v0, $zero, $s5
move $a0, $v0
li $v0, 17, 
syscall
jr $ra
###visit block end###
