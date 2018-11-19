.data
cArray: .space 12
######end of declaration######
.text
jal main
######declaration of functions######

main:
addi $sp, $sp, 0
move  $fp, $sp
###visit block###
###block vd↑###block stmt↓###
li $t9, 'h'
li $s7, 0
li $t8, 4
mult $t8, $s7
mflo $t8
la $s5,cArray
add $t8, $t8, $s5
sw $t9, ($t8), # visitAssign Tag 6
la $t8,cArray
# print_s
li $v0, 4
la $a0, ($t8)
syscall
# print_s

# print_i
li $t8, 8
li $t8, 1
sub $s7, $t8, $t8
li $v0, 1
add $a0, $zero, $s7
syscall
# print_i

li $s7, 0
add   $v0, $zero, $s7
move $a0, $v0
li $v0, 17, 
syscall
jr $ra
###visit block end###
