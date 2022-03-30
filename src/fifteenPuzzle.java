import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

class StateComparator implements Comparator<State> {
    @Override
    public int compare(State a, State b) {
        if (a.cost < b.cost)
            return -1;
        if (a.cost > b.cost)
            return 1;
        if(a.cost == b.cost){
            if (a.command.size() <b.command.size()){
                return 1;
            }
            else{
                return -1;
            }
        }
        return 0;
    }
}

class State{
    public State(int Cost, Vector<String> command) {
        this.cost = Cost;
        this.command = new Vector<>(command);
    }
    public int cost;
    public Vector<String> command;
}
public class fifteenPuzzle extends JPanel {
    // Size
    private int size;
    // tiles
    private int nTiles;
    // UI
    private int dimension;

    private int margin;
    // color
    private static final Color FOREGROUND_COLOR = new Color(0,0,0);

    private static final Random randomNumber = new Random();
    Vector<String> solution = new Vector<>();
    private boolean can_Solve;

    private int[] tiles;
    private int[] tempTiles;
    private int blankPos;
    PriorityQueue<State> pq = new PriorityQueue<State>(new StateComparator());
    private int gridSize;
    private int tileSize;
    private boolean gameOver;

    public fifteenPuzzle(int size, int dimension, int margin){
        this.size = size;
        this.dimension = dimension;
        this.margin = margin;
        nTiles = size *size -1;
        tiles = new int[size*size];

        gridSize = (dimension - 2 * margin);
        tileSize = gridSize/size;

        setPreferredSize(new Dimension(dimension, dimension + margin));
        setBackground(Color.WHITE);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("SansSerif", Font.BOLD, 60));
        this.gameOver = true;
        newGame();
    }

    public void newGame(){
        do{
            reset();
            shuffle();
        } while(!isSolvable());
    }

    public void reset(){
        for (int i =0;i<tiles.length;i++){
            tiles[i] = (i+1) % tiles.length;
        }
    }

    public void shuffle(){
        int n = nTiles;

        for(int i=0;i<16;i++)
        {
            tiles[i] = i+1;
        }
        tiles[0] =3;
        tiles[1]=10;
        tiles[2] = 1;
        tiles[9]=2;
//        //set last value as 16
//        //now i will shuffle array
//        for(int i=0;i<3;i++)
//        {
//            int index =  randomNumber.nextInt(16);
//            //replace element at random index with i index element
//            int temp = tiles[i];
//            tiles[i] = tiles[index];
//            tiles[index] = temp;
//        }
//        while (n > 1) {
//            int r = randomNumber.nextInt(n--);
//            int tmp = tiles[r];
//            tiles[r] = tiles[n];
//            tiles[n] = tmp;
//        }
        blankPos=findBlankPos(tiles);

    }
    public int kurang(){
        int count = 0;
        for (int i=0;i<16;i++){
            for (int j = i+1;j<16;j++){
                if(tiles[j]<tiles[i]){
                    count+=1;
                }
            }
        }
        System.out.println(count);
        return count;
    }

    public int positionBlankPos(){
        if ((blankPos <=3) || (blankPos<=11 &&blankPos>7)){
            return blankPos %2;
        }
        else{
            return (blankPos + 1)%2;
        }

    }

    public boolean isSolve(int[] array){
        for (int i=0;i<16;i++){
            if(array[i] != i+1){
                return false;
            }
        }
        return true;
    }

    
    public boolean isSolvable(){
        return (kurang() + positionBlankPos()) %2 ==0;
    }

    public void drawGrid(Graphics2D grid){
        for (int i =0; i< tiles.length;i++){
            int row = i/size;
            int col = i% size;

            int x = margin + col *tileSize;
            int y = margin + row * tileSize;

            if (tiles[i] == 16){
                if (gameOver){
                        grid.setColor(FOREGROUND_COLOR);
                }

                continue;
            }
            grid.setColor((getForeground()));
            grid.fillRoundRect(x,y,tileSize,tileSize,25,25);
            grid.setColor(Color.BLACK);
            grid.drawRoundRect(x,y,tileSize,tileSize,25,25);
            grid.setColor(Color.WHITE);

            drawCenteredString(grid, String.valueOf(tiles[i]), x, y);

        }
    }
    private void drawStartMessage(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FOREGROUND_COLOR);
            String s = "random";
//            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
////                    getHeight() - margin);
            g.drawString(s, (100) / 2,
                    getHeight() - margin);
        }
    }
    private void drawChooseFile(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FOREGROUND_COLOR);
            String s = "Choose File";
            g.drawString(s, (400) / 2,
                    getHeight() - margin);
        }
    }

    private void drawRunSolution(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FOREGROUND_COLOR);
            String s = "run solution";
//            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
////                    getHeight() - margin);
            g.drawString(s, (800) / 2,
                    getHeight() - margin);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        // center string s for the given tile (x,y)
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s,  x + (tileSize - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize - (asc + desc)) / 2));
    }

    private void solution(){
        Vector<String> tempCommand = new Vector<>();

        int tempCost;
        int i =0;


        while(true){
            tempTiles = this.tiles.clone();
            blankPos = findBlankPos(tempTiles);
//            System.out.print("temp:");
//            for (int j =0;j<16;j++){
//                System.out.print(tempTiles[j] + " ");
//            }
//            System.out.println(blankPos);
//            tempCommand.clear();
            if(pq.isEmpty()){
                if (boolUp()){
//                    System.out.println("up");
                    tempCommand.add("up");
                    commandTemp("up");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("down");
                    tempCommand.clear();
                }
                if(boolDown()){
//                    System.out.println("down");
                    tempCommand.add("down");
                    commandTemp("down");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("up");
                    tempCommand.clear();
                }
                if(boolRight()){
//                    System.out.println("right");
                    tempCommand.add("right");
                    commandTemp("right");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("left");
                    tempCommand.clear();
                }
                if(boolLeft()){
//                    System.out.println("left");
                    tempCommand.add("left");
                    commandTemp("left");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("right");
                    tempCommand.clear();
                }
//                while(!pq.isEmpty()){
//                    System.out.println(pq.poll().cost);
//                }
//                break;
            }
            else{


                State temp = pq.poll();
                commandTemp(temp.command);
                tempCommand = new Vector<>(temp.command);
                if (boolUp() && !tempCommand.get(tempCommand.size()-1).equals("down")){
//                    System.out.println("up");
                    tempCommand.add("up");
                    commandTemp("up");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println("ke-" + i);
//                    System.out.println(cost(tempTiles));
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("down");
                    tempCommand.remove(tempCommand.size()-1);
                }
                if(boolDown()&&!tempCommand.get(tempCommand.size()-1).equals("up")){
//                    System.out.println("down");
                    tempCommand.add("down");
                    commandTemp("down");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("up");
                    tempCommand.remove(tempCommand.size()-1);
                }
                if(boolRight()&&!tempCommand.get(tempCommand.size()-1).equals("left")){
//                    System.out.println("right");
                    tempCommand.add("right");
                    commandTemp("right");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("left");
                    tempCommand.removeElementAt(tempCommand.size()-1);
                }
                if(boolLeft()&&!tempCommand.get(tempCommand.size()-1).equals("right")){
//                    System.out.println("masuk sini");
                    tempCommand.add("left");
                    commandTemp("left");
                    tempCost = cost(tempTiles)+ tempCommand.size();
//                    System.out.println(tempCost);
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("right");
                    tempCommand.remove(tempCommand.size()-1);
                }

            }
        }
//        State temp;
//        while(!pq.isEmpty()){
//            temp = pq.poll();
//            System.out.println(temp.cost);
//            for (int j=0;j<temp.command.size();j++){
//                System.out.println(temp.command.get(j));
//            }
//        }
        System.out.println();
        for (int j=0;j<tempCommand.size();j++){
            System.out.println(tempCommand.get(j));
            command(tempCommand.get(j));
        }
    }
    public int findBlankPos(int[] tiles){
        for(int i=0;i<16;i++){
            if (tiles[i]==16){
                return i;
            }
        }
        return -1;
    }
    public int cost(int[] temp){
        int cost = 0;
        for(int i= 0;i<temp.length;i++){
            if(temp[i]!=i+1 &&  temp[i] != 16){
                cost++;
            }
        }
        return cost;
    }
    private void commandTemp(Vector<String> vectorCommand) {
        int temp;
        String command;
        for(int i =0;i<vectorCommand.size();i++){
            command = vectorCommand.get(i);
            if (command.equals("up")) {
                temp = tempTiles[blankPos - 4];
                tempTiles[blankPos - 4] = tempTiles[blankPos];
                tempTiles[blankPos] = temp;
                blankPos = blankPos - 4;
            } else if (command.equals("down")) {
                temp = tempTiles[blankPos + 4];
                tempTiles[blankPos + 4] = tempTiles[blankPos];
                tempTiles[blankPos] = temp;
                blankPos = blankPos + 4;
            } else if (command.equals("left")) {
                temp = tempTiles[blankPos - 1];
                tempTiles[blankPos - 1] = tempTiles[blankPos];
                tempTiles[blankPos] = temp;
                blankPos = blankPos - 1;
            } else if (command.equals("right")) {
                temp = tempTiles[blankPos + 1];
                tempTiles[blankPos + 1] = tempTiles[blankPos];
                tempTiles[blankPos] = temp;
                blankPos = blankPos + 1;
            } else {
                System.out.println("invalid Command");
            }
        }
    }
    private void commandTemp(String command) {
        int temp;
        if (command.equals("up")) {
            temp = tempTiles[blankPos - 4];
            tempTiles[blankPos - 4] = tempTiles[blankPos];
            tempTiles[blankPos] = temp;
            blankPos = blankPos - 4;
        } else if (command.equals("down")) {
            temp = tempTiles[blankPos + 4];
            tempTiles[blankPos + 4] = tempTiles[blankPos];
            tempTiles[blankPos] = temp;
            blankPos = blankPos + 4;
        } else if (command.equals("left")) {
            temp = tempTiles[blankPos - 1];
            tempTiles[blankPos - 1] = tempTiles[blankPos];
            tempTiles[blankPos] = temp;
            blankPos = blankPos - 1;
        } else if (command.equals("right")) {
            temp = tempTiles[blankPos + 1];
            tempTiles[blankPos + 1] = tempTiles[blankPos];
            tempTiles[blankPos] = temp;
            blankPos = blankPos + 1;
        } else {
            System.out.println("invalid Command");
        }
    }
    private void command(String command){
        int temp;
        if(command.equals("up")){
            temp = tiles[blankPos-4];
            tiles[blankPos-4] = tiles[blankPos];
            tiles[blankPos] = temp;
            blankPos = blankPos-4;
        }
        else if(command.equals("down")){
            temp = tiles[blankPos+4];
            tiles[blankPos+4] = tiles[blankPos];
            tiles[blankPos] = temp;
            blankPos = blankPos+4;
        }
        else if(command.equals("left")){
            temp = tiles[blankPos-1];
            tiles[blankPos-1] = tiles[blankPos];
            tiles[blankPos] = temp;
            blankPos = blankPos-1;
        }
        else if(command.equals("right")){
            temp = tiles[blankPos+1];
            tiles[blankPos+1] = tiles[blankPos];
            tiles[blankPos] = temp;
            blankPos = blankPos+1;
        }
        else{
            System.out.println("invalid Command");
        }
//        try {
//            Thread.sleep(1000);
//        }
//        catch(InterruptedException ex) {
//        }
        repaint();
    }

    public boolean boolLeft(){
        return blankPos % 4 != 0;
    }
    public boolean boolRight(){
        return blankPos % 4 != 3;
    }

    public boolean boolUp(){
        return blankPos-4>=0;
    }

    public boolean boolDown(){
        return blankPos+4<16;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2D);
        drawStartMessage(g2D);
        drawChooseFile(g2D);
        drawRunSolution(g2D);
    }

    public void actionPerformed(ActionEvent e){

    }

    public static void main(String[] args) {
        fifteenPuzzle game = new fifteenPuzzle(4,550,30);
        SwingUtilities.invokeLater(()->{
            JFrame frame =new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("GameOfFifteen");
            frame.setResizable(false);
            frame.add(game, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        game.solution();
    }
}

