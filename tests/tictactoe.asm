.data
a11: .space 4
a12: .space 4
a13: .space 4
a21: .space 4
a22: .space 4
a23: .space 4
a31: .space 4
a32: .space 4
a33: .space 4
empty: .space 4
######end of declaration######
.text
jal main
######declaration of functions######

reset:
addi $sp, $sp, 0
move  $t9, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $t9, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
###block vd↑###block stmt↓###
la $t2,empty
lb $s1,($t2)
la $t2, a11
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a12
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a13
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a21
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a22
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a23
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a31
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a32
sb $s1, ($t2), # visitAssign Tag 2
la $t2,empty
lb $s1,($t2)
la $t2, a33
sb $s1, ($t2), # visitAssign Tag 2
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
###visit block end###

full:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $s1, 0
sw $s1, -12($fp), # visitAssign Tag 3
la $t2,a11
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag1
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse1
ifTag1:
endElse1:
la $t2,a21
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag2
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse2
ifTag2:
endElse2:
la $t2,a31
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag3
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse3
ifTag3:
endElse3:
la $t2,a12
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag4
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse4
ifTag4:
endElse4:
la $t2,a22
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag5
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse5
ifTag5:
endElse5:
la $t2,a32
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag6
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse6
ifTag6:
endElse6:
la $t2,a13
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag7
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse7
ifTag7:
endElse7:
la $t2,a23
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag8
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse8
ifTag8:
endElse8:
la $t2,a33
lb $s1,($t2)
la $t7,empty
lb $t2,($t7)
sne $t7, $s1, $t2
beq $t7, $zero, ifTag9
lw $s1, -12($fp)
li $t2, 1
add $t8, $s1, $t2
sw $t8, -12($fp), # visitAssign Tag 3
b endElse9
ifTag9:
endElse9:
lw $s1, -12($fp)
li $t2, 9
seq $t7, $s1, $t2
beq $t7, $zero, ifTag10
li $s1, 1
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
b endElse10
ifTag10:
li $s1, 0
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
endElse10:
###visit block end###

set:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $s1, 1
sw $s1, -12($fp), # visitAssign Tag 3
lb $s1, -16($fp)
li $t2, 'a'
seq $t7, $s1, $t2
beq $t7, $zero, ifTag12
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 1
seq $t8, $s1, $t2
beq $t8, $zero, ifTag13
###visit block###
###block vd↑###block stmt↓###
la $t2,a11
lb $s1,($t2)
la $s2,empty
lb $t2,($s2)
seq $s2, $s1, $t2
beq $s2, $zero, ifTag14
lb $s1, -24($fp)
la $t2, a11
sb $s1, ($t2), # visitAssign Tag 2
b endElse14
ifTag14:
li $s1, 0
li $t2, 1
sub $t4, $s1, $t2
sw $t4, -12($fp), # visitAssign Tag 3
endElse14:
###visit block end###
b endElse13
ifTag13:
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 2
seq $s2, $s1, $t2
beq $s2, $zero, ifTag17
###visit block###
###block vd↑###block stmt↓###
la $t2,a12
lb $s1,($t2)
la $t4,empty
lb $t2,($t4)
seq $t4, $s1, $t2
beq $t4, $zero, ifTag18
lb $s1, -24($fp)
la $t2, a12
sb $s1, ($t2), # visitAssign Tag 2
b endElse18
ifTag18:
li $s1, 0
li $t2, 1
sub $s7, $s1, $t2
sw $s7, -12($fp), # visitAssign Tag 3
endElse18:
###visit block end###
b endElse17
ifTag17:
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 3
seq $t4, $s1, $t2
beq $t4, $zero, ifTag21
###visit block###
###block vd↑###block stmt↓###
la $t2,a13
lb $s1,($t2)
la $s7,empty
lb $t2,($s7)
seq $s7, $s1, $t2
beq $s7, $zero, ifTag22
lb $s1, -24($fp)
la $t2, a13
sb $s1, ($t2), # visitAssign Tag 2
b endElse22
ifTag22:
li $s1, 0
li $t2, 1
sub $t5, $s1, $t2
sw $t5, -12($fp), # visitAssign Tag 3
endElse22:
###visit block end###
b endElse21
ifTag21:
###visit block###
###block vd↑###block stmt↓###
li $s1, 0
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
endElse21:
###visit block end###
endElse17:
###visit block end###
endElse13:
###visit block end###
b endElse12
ifTag12:
###visit block###
###block vd↑###block stmt↓###
lb $s1, -16($fp)
li $t2, 'b'
seq $t8, $s1, $t2
beq $t8, $zero, ifTag26
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 1
seq $s2, $s1, $t2
beq $s2, $zero, ifTag27
###visit block###
###block vd↑###block stmt↓###
la $t2,a21
lb $s1,($t2)
la $t4,empty
lb $t2,($t4)
seq $t4, $s1, $t2
beq $t4, $zero, ifTag28
lb $s1, -24($fp)
la $t2, a21
sb $s1, ($t2), # visitAssign Tag 2
b endElse28
ifTag28:
li $s1, 0
li $t2, 1
sub $s7, $s1, $t2
sw $s7, -12($fp), # visitAssign Tag 3
endElse28:
###visit block end###
b endElse27
ifTag27:
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 2
seq $t4, $s1, $t2
beq $t4, $zero, ifTag31
###visit block###
###block vd↑###block stmt↓###
la $t2,a22
lb $s1,($t2)
la $s7,empty
lb $t2,($s7)
seq $s7, $s1, $t2
beq $s7, $zero, ifTag32
lb $s1, -24($fp)
la $t2, a22
sb $s1, ($t2), # visitAssign Tag 2
b endElse32
ifTag32:
li $s1, 0
li $t2, 1
sub $t5, $s1, $t2
sw $t5, -12($fp), # visitAssign Tag 3
endElse32:
###visit block end###
b endElse31
ifTag31:
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 3
seq $s7, $s1, $t2
beq $s7, $zero, ifTag35
###visit block###
###block vd↑###block stmt↓###
la $t2,a23
lb $s1,($t2)
la $t5,empty
lb $t2,($t5)
seq $t5, $s1, $t2
beq $t5, $zero, ifTag36
lb $s1, -24($fp)
la $t2, a23
sb $s1, ($t2), # visitAssign Tag 2
b endElse36
ifTag36:
li $s1, 0
li $t2, 1
sub $s3, $s1, $t2
sw $s3, -12($fp), # visitAssign Tag 3
endElse36:
###visit block end###
b endElse35
ifTag35:
###visit block###
###block vd↑###block stmt↓###
li $s1, 0
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
endElse35:
###visit block end###
endElse31:
###visit block end###
endElse27:
###visit block end###
b endElse26
ifTag26:
###visit block###
###block vd↑###block stmt↓###
lb $s1, -16($fp)
li $t2, 'c'
seq $s2, $s1, $t2
beq $s2, $zero, ifTag40
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 1
seq $t4, $s1, $t2
beq $t4, $zero, ifTag41
###visit block###
###block vd↑###block stmt↓###
la $t2,a31
lb $s1,($t2)
la $s7,empty
lb $t2,($s7)
seq $s7, $s1, $t2
beq $s7, $zero, ifTag42
lb $s1, -24($fp)
la $t2, a31
sb $s1, ($t2), # visitAssign Tag 2
b endElse42
ifTag42:
li $s1, 0
li $t2, 1
sub $t5, $s1, $t2
sw $t5, -12($fp), # visitAssign Tag 3
endElse42:
###visit block end###
b endElse41
ifTag41:
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 2
seq $s7, $s1, $t2
beq $s7, $zero, ifTag45
###visit block###
###block vd↑###block stmt↓###
la $t2,a32
lb $s1,($t2)
la $t5,empty
lb $t2,($t5)
seq $t5, $s1, $t2
beq $t5, $zero, ifTag46
lb $s1, -24($fp)
la $t2, a32
sb $s1, ($t2), # visitAssign Tag 2
b endElse46
ifTag46:
li $s1, 0
li $t2, 1
sub $s3, $s1, $t2
sw $s3, -12($fp), # visitAssign Tag 3
endElse46:
###visit block end###
b endElse45
ifTag45:
###visit block###
###block vd↑###block stmt↓###
lw $s1, -20($fp)
li $t2, 3
seq $t5, $s1, $t2
beq $t5, $zero, ifTag49
###visit block###
###block vd↑###block stmt↓###
la $t2,a33
lb $s1,($t2)
la $s3,empty
lb $t2,($s3)
seq $s3, $s1, $t2
beq $s3, $zero, ifTag50
lb $s1, -24($fp)
la $t2, a33
sb $s1, ($t2), # visitAssign Tag 2
b endElse50
ifTag50:
li $s1, 0
li $t2, 1
sub $s0, $s1, $t2
sw $s0, -12($fp), # visitAssign Tag 3
endElse50:
###visit block end###
b endElse49
ifTag49:
###visit block###
###block vd↑###block stmt↓###
li $s1, 0
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
endElse49:
###visit block end###
endElse45:
###visit block end###
endElse41:
###visit block end###
b endElse40
ifTag40:
###visit block###
###block vd↑###block stmt↓###
li $s1, 0
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
endElse40:
###visit block end###
endElse26:
###visit block end###
endElse12:
lw $s1, -12($fp)
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
###visit block end###

printGame:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
###block vd↑###block stmt↓###
.data
s1: .asciiz "\n"
.text
la $s1, s1
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s2: .asciiz "     1   2   3\n"
.text
la $s1, s2
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s3: .asciiz "   +---+---+---+\n"
.text
la $s1, s3
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s4: .asciiz "a  | "
.text
la $s1, s4
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a11
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s5: .asciiz " | "
.text
la $s1, s5
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a12
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s6: .asciiz " | "
.text
la $s1, s6
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a13
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s7: .asciiz " |\n"
.text
la $s1, s7
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s8: .asciiz "   +---+---+---+\n"
.text
la $s1, s8
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s9: .asciiz "b  | "
.text
la $s1, s9
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a21
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s10: .asciiz " | "
.text
la $s1, s10
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a22
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s11: .asciiz " | "
.text
la $s1, s11
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a23
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s12: .asciiz " |\n"
.text
la $s1, s12
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s13: .asciiz "   +---+---+---+\n"
.text
la $s1, s13
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s14: .asciiz "c  | "
.text
la $s1, s14
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a31
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s15: .asciiz " | "
.text
la $s1, s15
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a32
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s16: .asciiz " | "
.text
la $s1, s16
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

la $t2,a33
lb $s1,($t2)
# print_c
li $v0, 11
move $a0, $s1
syscall
# print_c

.data
s17: .asciiz " |\n"
.text
la $s1, s17
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s18: .asciiz "   +---+---+---+\n"
.text
la $s1, s18
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

.data
s19: .asciiz "\n"
.text
la $s1, s19
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
###visit block end###

printWinner:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
###block vd↑###block stmt↓###
.data
s20: .asciiz "Player "
.text
la $s1, s20
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

# print_i
lw $s1, -12($fp)
li $v0, 1
add $a0, $zero, $s1
syscall
# print_i

.data
s21: .asciiz " has won!\n"
.text
la $s1, s21
# print_s
li $v0, 4
la $a0, ($s1)
syscall
# print_s

# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
###visit block end###

switchPlayer:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
###block vd↑###block stmt↓###
lw $s1, -12($fp)
li $t2, 1
seq $t7, $s1, $t2
beq $t7, $zero, ifTag54
li $s1, 2
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
b endElse54
ifTag54:
li $s1, 1
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
endElse54:
###visit block end###

get_mark:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
###block vd↑###block stmt↓###
lw $s1, -12($fp)
li $t2, 1
seq $t7, $s1, $t2
beq $t7, $zero, ifTag56
li $s1, 'X'
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
b endElse56
ifTag56:
li $s1, 'O'
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
endElse56:
###visit block end###

selectmove:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $s1, 1
sw $s1, -20($fp), # visitAssign Tag 3
whileStart1:
lw $s1, -20($fp)
beq $s1, $zero, whileEnd1
###visit block###
###block vd↑###block stmt↓###
.data
s22: .asciiz "Player "
.text
la $t2, s22
# print_s
li $v0, 4
la $a0, ($t2)
syscall
# print_s

# print_i
lw $t2, -32($fp)
li $v0, 1
add $a0, $zero, $t2
syscall
# print_i

.data
s23: .asciiz " select move (e.g. a2)>"
.text
la $t2, s23
# print_s
li $v0, 4
la $a0, ($t2)
syscall
# print_s

# read_c
li $v0, 12
syscall
# read_c

add $t2 $v0, $zero
sb $t2, -12($fp), # visitAssign Tag 4
# read_i
li $v0, 5
syscall
# read_i

add $t2 $v0, $zero
sw $t2, -16($fp), # visitAssign Tag 3
lw $t7, -32($fp)
sw $t7, -12($sp)
jal get_mark
add  $t7, $zero, $v0
sb $t7, -28($fp), # visitAssign Tag 4
lb $t8, -12($fp)
sb $t8, -16($sp)
lw $s2, -16($fp)
sw $s2, -20($sp)
lb $t4, -28($fp)
sb $t4, -24($sp)
jal set
add  $t4, $zero, $v0
sw $t4, -24($fp), # visitAssign Tag 3
lw $t4, -24($fp)
li $s7, 0
seq $t5, $t4, $s7
beq $t5, $zero, ifTag58
###visit block###
###block vd↑###block stmt↓###
.data
s24: .asciiz "That is not a valid move!\n"
.text
la $t4, s24
# print_s
li $v0, 4
la $a0, ($t4)
syscall
# print_s

###visit block end###
b endElse58
ifTag58:
###visit block###
###block vd↑###block stmt↓###
lw $t4, -24($fp)
li $s7, 0
li $s3, 1
sub $s0, $s7, $s3
seq $s7, $t4, $s0
beq $s7, $zero, ifTag60
.data
s25: .asciiz "That move is not possible!\n"
.text
la $t4, s25
# print_s
li $v0, 4
la $a0, ($t4)
syscall
# print_s

b endElse60
ifTag60:
li $t4, 0
sw $t4, -20($fp), # visitAssign Tag 3
endElse60:
###visit block end###
endElse58:
###visit block end###
b whileStart1
whileEnd1:
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
###visit block end###

won:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
# Save fp,sp,rt
sw $s1, 0($fp)
sw $fp, -4($fp)
sw $ra, -8($sp)
add $sp, $sp, -12
# End of save fp,sp,rt
###visit block###
add $sp, $sp, -4
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $s1, 0
sw $s1, -12($fp), # visitAssign Tag 3
la $t4,a11
lb $s1,($t4)
lb $t4, -16($fp)
seq $t5, $s1, $t4
beq $t5, $zero, ifTag62
###visit block###
###block vd↑###block stmt↓###
la $t4,a21
lb $s1,($t4)
lb $t4, -16($fp)
seq $s7, $s1, $t4
beq $s7, $zero, ifTag63
###visit block###
###block vd↑###block stmt↓###
la $t4,a31
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag64
###visit block###
###block vd↑###block stmt↓###
li $t4, 1
sw $t4, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse64
ifTag64:
endElse64:
###visit block end###
b endElse63
ifTag63:
###visit block###
###block vd↑###block stmt↓###
la $t4,a22
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag66
###visit block###
###block vd↑###block stmt↓###
la $s1,a33
lb $t4,($s1)
lb $s1, -16($fp)
seq $s0, $t4, $s1
beq $s0, $zero, ifTag67
###visit block###
###block vd↑###block stmt↓###
li $s1, 1
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse67
ifTag67:
endElse67:
###visit block end###
b endElse66
ifTag66:
###visit block###
###block vd↑###block stmt↓###
la $t4,a12
lb $s1,($t4)
lb $t4, -16($fp)
seq $s0, $s1, $t4
beq $s0, $zero, ifTag69
###visit block###
###block vd↑###block stmt↓###
la $s1,a13
lb $t4,($s1)
lb $s1, -16($fp)
seq $t0, $t4, $s1
beq $t0, $zero, ifTag70
###visit block###
###block vd↑###block stmt↓###
li $s1, 1
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse70
ifTag70:
endElse70:
###visit block end###
b endElse69
ifTag69:
endElse69:
###visit block end###
endElse66:
###visit block end###
endElse63:
###visit block end###
b endElse62
ifTag62:
endElse62:
la $t4,a12
lb $s1,($t4)
lb $t4, -16($fp)
seq $t5, $s1, $t4
beq $t5, $zero, ifTag71
###visit block###
###block vd↑###block stmt↓###
la $t4,a22
lb $s1,($t4)
lb $t4, -16($fp)
seq $s7, $s1, $t4
beq $s7, $zero, ifTag72
###visit block###
###block vd↑###block stmt↓###
la $t4,a32
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag73
###visit block###
###block vd↑###block stmt↓###
li $t4, 1
sw $t4, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse73
ifTag73:
endElse73:
###visit block end###
b endElse72
ifTag72:
endElse72:
###visit block end###
b endElse71
ifTag71:
endElse71:
la $t4,a13
lb $s1,($t4)
lb $t4, -16($fp)
seq $t5, $s1, $t4
beq $t5, $zero, ifTag74
###visit block###
###block vd↑###block stmt↓###
la $t4,a23
lb $s1,($t4)
lb $t4, -16($fp)
seq $s7, $s1, $t4
beq $s7, $zero, ifTag75
###visit block###
###block vd↑###block stmt↓###
la $t4,a33
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag76
###visit block###
###block vd↑###block stmt↓###
li $t4, 1
sw $t4, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse76
ifTag76:
endElse76:
###visit block end###
b endElse75
ifTag75:
###visit block###
###block vd↑###block stmt↓###
la $t4,a22
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag78
###visit block###
###block vd↑###block stmt↓###
la $s1,a31
lb $t4,($s1)
lb $s1, -16($fp)
seq $s0, $t4, $s1
beq $s0, $zero, ifTag79
###visit block###
###block vd↑###block stmt↓###
li $s1, 1
sw $s1, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse79
ifTag79:
endElse79:
###visit block end###
b endElse78
ifTag78:
endElse78:
###visit block end###
endElse75:
###visit block end###
b endElse74
ifTag74:
endElse74:
la $t4,a21
lb $s1,($t4)
lb $t4, -16($fp)
seq $t5, $s1, $t4
beq $t5, $zero, ifTag80
###visit block###
###block vd↑###block stmt↓###
la $t4,a22
lb $s1,($t4)
lb $t4, -16($fp)
seq $s7, $s1, $t4
beq $s7, $zero, ifTag81
###visit block###
###block vd↑###block stmt↓###
la $t4,a23
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag82
###visit block###
###block vd↑###block stmt↓###
li $t4, 1
sw $t4, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse82
ifTag82:
endElse82:
###visit block end###
b endElse81
ifTag81:
endElse81:
###visit block end###
b endElse80
ifTag80:
endElse80:
la $t4,a31
lb $s1,($t4)
lb $t4, -16($fp)
seq $t5, $s1, $t4
beq $t5, $zero, ifTag83
###visit block###
###block vd↑###block stmt↓###
la $t4,a32
lb $s1,($t4)
lb $t4, -16($fp)
seq $s7, $s1, $t4
beq $s7, $zero, ifTag84
###visit block###
###block vd↑###block stmt↓###
la $t4,a33
lb $s1,($t4)
lb $t4, -16($fp)
seq $s3, $s1, $t4
beq $s3, $zero, ifTag85
###visit block###
###block vd↑###block stmt↓###
li $t4, 1
sw $t4, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse85
ifTag85:
endElse85:
###visit block end###
b endElse84
ifTag84:
endElse84:
###visit block end###
b endElse83
ifTag83:
endElse83:
lw $s1, -12($fp)
add   $v0, $zero, $s1
# restore fp,sp,rt
lw $sp, -4($fp)
lw $ra, -8($fp)
lw $fp, 0($fp)
# restore of saved fp,sp,rt
jr $ra
###visit block end###

main:
addi $sp, $sp, 0
move  $s1, $fp
move  $fp, $sp
add $sp, $sp, -12
###visit block###
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
add $sp, $sp, -4
###block vd↑###block stmt↓###
li $s1, ' '
la $t4, empty
sb $s1, ($t4), # visitAssign Tag 2
li $s1, 1
sw $s1, -12($fp), # visitAssign Tag 3
jal reset
jal printGame
li $s1, 1
sw $s1, -16($fp), # visitAssign Tag 3
whileStart2:
lw $s1, -12($fp)
beq $s1, $zero, whileEnd2
###visit block###
###block vd↑###block stmt↓###
lw $t5, -16($fp)
sw $t5, -32($sp)
jal selectmove
lw $s7, -16($fp)
sw $s7, -12($sp)
jal get_mark
add  $s7, $zero, $v0
sb $s7, -20($fp), # visitAssign Tag 4
jal printGame
lb $s3, -20($fp)
sb $s3, -16($sp)
jal won
add  $s3, $zero, $v0
beq $s3, $zero, ifTag86
###visit block###
###block vd↑###block stmt↓###
lw $t0, -16($fp)
sw $t0, -12($sp)
jal printWinner
li $t0, 0
sw $t0, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse86
ifTag86:
jal full
add  $t0, $zero, $v0
li $t6, 1
seq $s4, $t0, $t6
beq $s4, $zero, ifTag88
###visit block###
###block vd↑###block stmt↓###
.data
s26: .asciiz "It\'s a draw!\n"
.text
la $t0, s26
# print_s
li $v0, 4
la $a0, ($t0)
syscall
# print_s

li $t0, 0
sw $t0, -12($fp), # visitAssign Tag 3
###visit block end###
b endElse88
ifTag88:
###visit block###
###block vd↑###block stmt↓###
lw $t6, -16($fp)
sw $t6, -12($sp)
jal switchPlayer
add  $t6, $zero, $v0
sw $t6, -16($fp), # visitAssign Tag 3
###visit block end###
endElse88:
endElse86:
lw $s3, -12($fp)
li $t6, 0
seq $s4, $s3, $t6
beq $s4, $zero, ifTag90
###visit block###
###block vd↑###block stmt↓###
.data
s27: .asciiz "Play again? (y/n)> "
.text
la $s3, s27
# print_s
li $v0, 4
la $a0, ($s3)
syscall
# print_s

# read_c
li $v0, 12
syscall
# read_c

add $s3 $v0, $zero
sb $s3, -24($fp), # visitAssign Tag 4
lb $s3, -24($fp)
li $t6, 'y'
seq $t3, $s3, $t6
beq $t3, $zero, ifTag91
###visit block###
###block vd↑###block stmt↓###
li $s3, 1
sw $s3, -12($fp), # visitAssign Tag 3
jal reset
###visit block end###
b endElse91
ifTag91:
###visit block###
###block vd↑###block stmt↓###
lb $s3, -24($fp)
li $t6, 'Y'
seq $s5, $s3, $t6
beq $s5, $zero, ifTag93
###visit block###
###block vd↑###block stmt↓###
li $s3, 1
sw $s3, -12($fp), # visitAssign Tag 3
jal reset
###visit block end###
b endElse93
ifTag93:
endElse93:
###visit block end###
endElse91:
###visit block end###
b endElse90
ifTag90:
endElse90:
###visit block end###
b whileStart2
whileEnd2:
li $v0, 10
syscall
jr $ra
###visit block end###
