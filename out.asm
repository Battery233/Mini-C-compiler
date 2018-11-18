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
add $sp, $sp, -4
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $t9, 'x'
sb $t9, 12($fp), # visitAssign Tag 4
li $t9, 'h'
la $s6,cArray
li $s6, 0
li $t8, 4
mult $t8, $s6
mflo $t8
add $t8, $t8, $s6
sw $t9, ($t8), # visitAssign Tag 6
li $t9, 'i'
la $s6,cArray
li $s6, 1
li $t8, 4
mult $t8, $s6
mflo $t8
add $t8, $t8, $s6
sw $t9, ($t8), # visitAssign Tag 6
li $t9, '!'
la $s6,cArray
li $s6, 2
li $t8, 4
mult $t8, $s6
mflo $t8
add $t8, $t8, $s6
sw $t9, ($t8), # visitAssign Tag 6
li $t9, 666
sw $t9, 4($fp), # visitAssign Tag 3
li $t9, 660
sw $t9, 8($fp), # visitAssign Tag 3
whileStart1:
lw $t9, 8($fp)
lw $t8, 4($fp)
slt $s6, $t9, $t8
beq $s6, $zero, whileEnd1
###visit block###
###block vd↑###block stmt↓###
lw $t8, 8($fp)
# print_i
li $v0, 1
add $a0, $zero, $t8
syscall
# print_i

lw $t8, 8($fp)
li $t9, 1
add $s6, $t8, $t9
sw $s6, 8($fp), # visitAssign Tag 3
###visit block end###
b whileStart1
whileEnd1:
lb $s6, 12($fp)
# print_c
li $v0, 11
move $a0, $s6
syscall
# print_c

li $s6, 1
li $s6, 2
sgt $t9, $s6, $s6
beq $t9, $zero, ifTag1
lw $s6, 4($fp)
# print_i
li $v0, 1
add $a0, $zero, $s6
syscall
# print_i

b endElse1
ifTag1:
lw $s6, 4($fp)
li $s6, 1
add $t8, $s6, $s6
# print_i
li $v0, 1
add $a0, $zero, $t8
syscall
# print_i

endElse1:
li $t9, 0
li $t8, 1
sub $s6, $t9, $t8
add   $v0, $zero, $s6
move $a0, $v0
li $v0, 17, 
syscall
jr $ra
li $s6, 0
add   $v0, $zero, $s6
move $a0, $v0
li $v0, 17, 
syscall
jr $ra
###visit block end###
