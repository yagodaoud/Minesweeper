import br.com.yagodaoud.ms.gui.BoardConsole;
import br.com.yagodaoud.ms.model.Board;

public class Application {

    public static void main(String[] args) {

        Board board = new Board(6, 6, 6);
        new BoardConsole(board);
    }
}
