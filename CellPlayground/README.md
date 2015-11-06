This is the first assignment for our University class on Concurrent and Event-Based Programming.
It is an implementantion of the "Game of Life". It simulates cells inside a playground. The cells 
feed on special 'food cells'. At the right time, they can divide, via sexual or asexual division.

Rules of the game
------------------
Jocul vietii

Simulati o populatie de celule vii ce au fiecare ca scop hranirea si inmultirea.

Exista un numar limitat de unitati de hrana (resurse) pe care celulele le consuma. O unitate de hrana ii ajunge unei celule un timp dat T_full dupa care i se face foame. Daca nu maninca un alt timp dat T_starve, celula moare rezultind un numar aleator intre 1 si 5 de unitati de hrana.

Dupa ce a mincat de minim 10 ori, o celula se va inmulti inainte sa i se faca din nou foame. Exista doua tipuri de celule: sexuate si asexuate. Celulele asexuate se inmultesc prin diviziune, rezultind doua celule flaminde.

Cele sexuate se inmultesc doar daca gasesc o alta celula ce cauta sa se reproduca iar atunci rezulta o a treia celula, initial infometata.

In simulare, celulele vor fi reprezentate de fire de executie distincte.

Game mechanics
--------------
- The playground is set up and a fixed amount of food cells and alive cells are spawned.
- Cells move in a random direction, and if hungry, absorb food cells when they touch.
- A hungry cell can be distinguished by having a gray outline.
- Once a live cell has fed 10 times or more, it will try to reproduce.
- Non-sexual division is done by splitting into two smaller, hungry cells.
- Sexual division is done by finding another sexual cell ready for division. On intersection of the two, a third sexual, hungry cell will be spawned.
- Live cells have a time while not hungry. During this period they will not absorb food cells.
- They also have a time until starvation. If this timer expires, they will die, leaving a random number between 1-5 of food cells behind.

