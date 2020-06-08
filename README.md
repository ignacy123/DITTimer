# DITTimer
### To run:
enter /target and execute (or wherever the jar is, if you downloaded it without cloning the repo):

java --module-path <path to javafx-sdk/lib> --add-modules=javafx.controls,javafx.base,javafx.fxml,javafx.media -jar DITTimer-1.0-SNAPSHOT-jar-with-dependencies.jar


### Grading:

Since project is using HSQLDB database in non read-only mode running multiple instances of application is not recommended. We are aware that for grading purposes there should be a way to enter an online room with multiple clients from one machine and we decided to create a Client class in Server/view/start. It goes directly to room choosing menu skipping all db related code. Same goes for server - 'final' server is set up on azure and application connects with it automatically. If you wish to run server locally it is in main/java/server package. You also need to uncomment the line in ServerServiceImplementation in main/java/conn/
in method start which is connecting to localhost rather than azure server (it's quite obvious which one). 
As you need to modify the code to do this, you need to clone the repo first (in other case we would have generate multiple jars, which would be confusing).
However, we want to stress that these classes are part of a bigger whole in final project and such access is left purely for grading purposes.


### IMPORTANT:
All averages in application are counted without the best and the worst time (average of 5 is really an average of the middle 3 solves)

DNF stands for Did Not Finish and the time is automatically counted as the worst..

### All features:
As some features are not apparent I will briefly describe what application is capable of:

#### Offline mode:
Counting time - space to start/stop. Buttons below are quite self-explanatory

Time list - first column are single times, second columns are averages of 5 (i-th row is average of i-4-th, i-3-rd, i-2-nd, i-1-st and i-th solve), and the third column show averages of 12 You can double click a cell to see more info.

##### Metronome

When you press ON, it's not going to run immediataly. It just beeps when the time is counting (a more detailed explanation why it's like that will be down below).
The slider is self-explanatory.

##### Graphs
They just show the line for single times, averages of 5 and averages of 12.

##### A big cube scheme in top-right corner.

It shows what the cube should look like after performing the scrambling algorithm which is above time counter. Used to prevent so-called "misscrambles".
 
##### More...

Importing/exporting the times both locally and to the server do exactly what they say. User just chooses which one he wants to use depending whether he has internet connection or not.

Practice features - buttons do what they say, more explanations below.

Online part - the room choosing menu allows you to create a room, or join an existing one. You can choose your nickname (for both creating and joining). When creating a
room you can make it private or public (private requires choosing a password). To join a room you have to double click it on the list.

Online room - it's meant to practice together with friends/random people. Everyone has the same scrambling algorithm (so you can discuss your solutions ect.) and everyone can see each other's times.

List of users, leave room button, lists of times, cube scheme, send time button - they do what they are meant to do.

Next scramble button - if you can see it it means you are the host in the lobby. It has two functionalities - once all your friends join, you start the practice with it (the first scrambling algorithm is sent to everyone). Since then, a new scrambling algorithm is sent *automatically*, 
when everyone is done with the previous one. 

The second functionality is a "last resort" button - if someone joins the lobby and doesn't send times on purpose blocking the automatic sending of scrambling algorithms. It just allows you to sent a new one manually.

Two weird rows below lists of times - they just show average of 5 (up) and average of 12 (down). The best one has a green colour (to support slight competition between users).


### Explanation why things are the way they are:

Metronome - why does it run only when time is on? Speedsolving of rubik's cube consists of two crucial abilities
recognition and execution. We have a natural tendency to focus on execution only
(turning the cube stupidly but fast) without caring about our recognition skills.
You can use metronome to pace your turning speed (do one move with every beep)
and allow your brain to gradually adjust to increasing execution speed and minimize the pauses when transitioning between steps.


PLL recognition checker. PLL is the last step in the most popular solving method (CFOP). It's just about recognition, no thinking is involved.
There are 21 cases and each has 4 representations - normal one, normal one but top face is rotated by 90 degrees, 180 or 270. They are all solved with same algorithm but look completely differently.
This exercise focuses on recognition of the case so that you can go straight into execution.
Each of 21 cases has a name, and those names are on the buttons.

Correct state checker - this is meant for beginners. If you disasemble a cube
and assemble it randomly, there's a big chance it's going to be unsolvable.
This tool is meant for people who don't know how to solve a cube yet, and they dug out an old one, to which anything could have happened.
Once you paint the scheme the tool will tell if the cube is solvable, and if not, what you need to do to make it solvable (swap two edges, flip a corner, etc.)

Winter variation learner - most popular method, CFOP, consists of 7 steps.
There's a big set of cases that can appear during the 5th step, but some are special,
and with some extra work you can also skip the 6th step. This tool 
helps you memorize these extra steps. They show a succession of moves
to prepare a case, and then number of solutions to solve it - all in order
to practice muscle memory. Winter variation is the name of this subset with
special cases.

Appending times when importing - it's done to allow user to merge times from two different machines. Deleting old times when uploading would be irreversible - now user has a choice if he wants to keep or remove old times.

Turning off online client doesn't take you back to offline timer - done to support using multiple clients to grade and avoid running multiple non read-only connections to HSQLDB database which would cause an error.
