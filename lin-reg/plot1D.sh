set size square
 
set ylabel "f(x)"
set xlabel "x"

set terminal jpeg
set output "./image/1D-fit-plot.jpg"

f(x)=a*x+b+0.000000001
title_f(a,b) = sprintf('f(x) = %.2fx +  %.2f', a, b)

fit f(x) "data/functional-test.dat" u 1:2:3 via a,b
plot f(x) t title_f(a,b),"data/functional-test.dat" using 1:2 title "1D-fit-plot" with points


