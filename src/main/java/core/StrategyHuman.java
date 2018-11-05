package core;

import java.util.ArrayList;
import java.util.Scanner;

public class StrategyHuman extends PlayBehaviour {
    ArrayList<Meld> determineInitialMove(Hand hand) {
        if (this.handleInput(hand)) {
            return this.workspace;
        }
        return null;
    }

    ArrayList<Meld> determineRegularMove(Hand hand) {
        if (this.handleInput(hand)) {
            return this.workspace;
        }
        return null;
    }

    private boolean handleInput(Hand hand) {
        Scanner scanner = new Scanner(System.in);
        String choice   = new String();

        while (true) {
            System.out.print("Draw (d) a tile or play (p): ");
            choice = scanner.nextLine();

            if (choice.equals("p")) {
                while (true) {
                    System.out.print("Enter move: ");
                    choice = scanner.nextLine();

                    if (choice.equals("f")) {
                        break;
                    }
                    this.parseInput(hand, choice);
                }
                break;
            } else if (choice.equals("d")) {
                return false;
            } else {
                System.out.println("Invalid command.");
            }
        }

        return true;
    }

    protected void parseInput(Hand hand, String input) {
        String[] commands   = input.split(">");
        String tilesToMove  = commands[0].trim();
        String meldToUpdate = commands[1].trim();

        Meld meld;
        // Determine if we're operating on a new meld or an existing one
        if (meldToUpdate.equals("NM")) {
            meld = new Meld();
        } else {
            // Subtract 1 because melds are indexed from 1 on the table
            meld = this.workspace.get(Integer.parseInt(meldToUpdate.substring(1)) - 1);
        }

        for (String tileString : tilesToMove.split(" ")) {
            // Move a tile from an existing meld on the table
            if (tileString.length() >= 4) {
                int meldIndex = 0;
                int tileIndex = 0;
                // Determine the first occurence of a tile in the string (which will be its colour)
                // The index before that is the last
                // This is done in a loop to account for selecting melds that are > 10
                // Examples:
                //     M1R3  -> meldIndex = 1, tileIndex = 2
                //     M10R5 -> meldIndex = 2, tileIndex = 3
                for (int i = 0; i < tileString.length(); i++) {
                    if (tileString.charAt(i) == 'R' ||
                        tileString.charAt(i) == 'G' ||
                        tileString.charAt(i) == 'B' ||
                        tileString.charAt(i) == 'O') {
                        meldIndex = i - 1;
                        tileIndex = i;
                        break;
                    }
                }
                // Determine the meld index (i.e. 1 from M1, 13 from M13)
                meldIndex = Integer.parseInt(tileString.substring(1, tileIndex));
                Tile tile = new Tile(tileString.substring(tileIndex));
                // Melds are indexed by 1 in the workspace
                Meld meldToRemoveFrom = this.workspace.get(meldIndex - 1);
                tile                  = meldToRemoveFrom.removeTileObject(tile);
                meld.addTile(tile);
            // Split a meld on the table
            } else if (tileString.contains("|")) {
                int meldIndex = Integer.parseInt(tileString.substring(1, tileString.indexOf(" ")));
                // M1 | R3 (+ 2 after the pipe is our number)
                Tile tileToSplitAt = new Tile(tileString.substring(tileString.indexOf("|") + 2));
                Meld meldToSplit   = this.workspace.get(meldIndex);

                // Find where in the meld to split
                int tileIndex = 0;
                for (int i = 0; i < meldToSplit.getSize(); i++) {
                    Tile meldTile = meld.getTile(i);

                    if (meldTile.getValue() == tileToSplitAt.getValue() &&
                        meldTile.getColour() == tileToSplitAt.getColour()) {
                        tileIndex = i;
                        break;
                    }
                }

                Meld otherMeld = meld.splitMeld(tileIndex);
                this.workspace.add(otherMeld);
            // Move a tile from the player's hand
            } else {
                Tile tile = new Tile(tileString);
                meld.addTile(hand.remove(tile));
            }
        }

        // If a new meld was created, add it to the workspace
        if (meldToUpdate.equals("NM")) {
            this.workspace.add(meld);
        }
    }
}
