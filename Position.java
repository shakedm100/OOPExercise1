public class Position {
    private int row, col;

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public Position(Position position)
    {
        this.row = position.row();
        this.col = position.col();
    }

    public int row()
    {
        return row;
    }

    public int col()
    {
        return col;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setCol(int col)
    {
        this.col = col;
    }
}
