Farkle is a dice game where the goal is for a player to reach 10,000 points before the other players via different scoring combinations. This project is a Java command-line version of this game.  This game can be played with anywhere from one to an infinite number of players. To play, compile all java files and then run java Farkle command.

Rules:
The goal of Farkle is to reach 10,000 points before other players. In a single turn
a player may keep rolling as long as they score some scoring combination of dice every
roll.  At least one scoring point combination must be kept every roll to keep rolling. 
The dice associated with each scoring combination are set aside before the next roll.
Once all 6 dice have been tied up in point combinations during a turn, the player must reroll all 6 and 
keep going one more roll no matter what.  The point totals from the previous rolls are kept 
as the player rerolls with all 6 dice.  Until a player initially reaches 1,000 points, they 
may not stop their turn and bank their points.  However, once the 1,000 point mark has been
reached in one turn, the player may choose to stop their turn at any time and bank that
current turn's points to the scoreboard, as well as stop an future turn at any point and bank
any points on hand.  If a player rolls no scoring combination of dice on a roll, that is called
a Farkle and their turn is over. The first player to reach 10,000 points wins the game!
            
Possible point combinations: 
**One** - 100 points

**Five** - 50 points

**3-6 of a Kind** - Three 1's equals 1,000 points, every additional 1 after that doubles the points.
                Three of anything other than a 1 equals that number multiplied by 100, every additional
                dice of that number after that doubles the points.
                
**Three pairs of Two** - 1,500 points

**Two pairs of Three** - 1,500 points

**Straight** - 2,500 points
