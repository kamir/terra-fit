set size square
 
set xlabel "x"
set ylabel "y"
set zlabel "f(x,y)"

set terminal jpeg
set output "image/2D-fit-plot.jpg"

f(x,y)=a*x+b*y+c+0.000000001
title_f(a,b,c) = sprintf('f(x,y) = %.2fx + %.2fy + %.2f', a, b, c)


fit f(x,y) "data/functional-test.dat" u 1:2:3 via a,b,c
splot f(x,y) t title_f(a,b,c),"data/functional-test.dat" using 1:2:3 title "2D-fit-plot" with points


