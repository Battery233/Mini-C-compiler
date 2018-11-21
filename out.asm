.data
######end of declaration######
.text
jal main
######declaration of functions######

foo:
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
add $sp, $sp, -4
###block vd↑###block stmt↓###
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
###block vd↑###block stmt↓###
li $s1, 3
sw $s1, -12($fp), # visitAssign Tag 3
li $s1, 5
sw $s1, -16($fp), # visitAssign Tag 3
# print_i
lw $s1, -16($fp)
li $v0, 1
add $a0, $zero, $s1
syscall
# print_i

li $t2, 3
sw $t2, -12($sp)
jal foo
li $t2, 0
add   $v0, $zero, $t2
li $v0, 17
syscall
jr $ra
###visit block end###
