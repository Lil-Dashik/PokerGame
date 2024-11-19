package PokerGame;


public class PokerGame {
    public static void main(String[] args) {
        DealerDeal dealer = new DealerDeal(new Deck());
        try {
            Board board = dealer.dealCardsToPlayers();


            // Раздача флопа
            board = dealer.dealFlop(board);


            // Раздача терна
            board = dealer.dealTurn(board);


            // Раздача ривера
            board = dealer.dealRiver(board);
            System.out.println(board.toString());
            dealer.decideWinner(board);
        } catch (InvalidPokerBoardException e) {
            System.out.println(e.getMessage());
        }
    }
}

