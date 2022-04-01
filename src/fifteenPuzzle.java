import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class fifteenPuzzle extends JPanel {
    // Size
    private static final int size = 4;
    // tiles
    private int nTiles;
    // UI
    private int dimension;

    private int margin;
    // color
    private static final Color FOREGROUND_COLOR = new Color(255,182,213);
    private static final Color BACKGROUND_COLOR = new Color(255,217,225);
    private static final Random randomNumber = new Random();

    private boolean can_Solve;

    private int[] tiles;
    private int blankPos;

    private int gridSize;
    private int tileSize;
    private boolean gameOver;

    // for solution
    private PriorityQueue<State> pq = new PriorityQueue<State>(new StateComparator());
    private Vector<String> solutionCommand = new Vector<>();
    private int[] tempTiles;

    // create fifteen puzzle
    public fifteenPuzzle(int dimension, int margin){
        this.dimension = dimension;
        this.margin = margin;
        nTiles = size *size;
        tiles = new int[size*size];
        tempTiles = new int[size*size];
        gridSize = (dimension - 2 * margin);
        tileSize = gridSize/size;

        setPreferredSize(new Dimension(this.dimension, this.dimension + margin));
        setBackground(BACKGROUND_COLOR);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("SansSerif", Font.BOLD, 60));
        this.gameOver = true;
        setDefault();
    }

    public void newGame(){
        do{
            setDefault();
            shuffle();
        } while(!isSolvable());
        repaint();
    }

    public void setDefault(){
        for(int i=0;i<16;i++)
        {
            tiles[i] = i+1;
            tempTiles[i] = i+1;
        }
    }

    public void shuffle(){
        int i =0;
        while(i<25){
            int index =  randomNumber.nextInt(0,100) %4;
            if(index == 0 && boolUp()){
                commandTemp("up");
            }
            else if(index == 1 && boolDown()){
                commandTemp("down");
            }
            else if(index == 2 && boolRight()){
                commandTemp("right");
            }
            else if(index == 3 && boolLeft()){
                commandTemp("left");
            }
            else{
                i--;
            }
            i++;
        }
        for (int j =0;j<16;j++){
            tiles[j] = tempTiles[j];
        }
        blankPos=findBlankPos(tiles);
    }

    // function KURANG(X)
    public int kurang(){
        int count = 0;
        for (int i=0;i<16;i++){
            for (int j = i+1;j<16;j++){
                if(tiles[j]<tiles[i]){
                    count+=1;
                }
            }
        }
        return count;
    }

    // X begining position of blankPos
    public int positionBlankPos(){
        if ((blankPos <=3) || (blankPos<=11 &&blankPos>7)){
            return blankPos %2;
        }
        else{
            return (blankPos + 1)%2;
        }

    }

    // tiles solve
    public boolean isSolve(int[] array){
        for (int i=0;i<16;i++){
            if(array[i] != i+1){
                return false;
            }
        }
        return true;
    }

    // tiles can solve
    public boolean isSolvable(){
        int sum = kurang() + positionBlankPos();
        if(sum %2 ==0){
            System.out.println("Nilai Kurang(i) :" + kurang());
            System.out.println("Kurang(i) + X   :" + sum);
        }
        
        return (sum) %2 ==0;
    }

    // daraw the grid
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
            grid.fillRoundRect(x,y,tileSize-10,tileSize-10,0,0);
            grid.setColor(Color.gray);
            grid.drawRoundRect(x,y,tileSize-10,tileSize-10,0,0);
            grid.setColor(Color.WHITE);

            drawCenteredString(grid, String.valueOf(tiles[i]), x, y);

        }
    }

    private void drawStartMessage(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FOREGROUND_COLOR);
            String s = "random";
            g.drawString(s, (100) / 2,
                    getHeight() - margin);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        // center string s for the given tile (x,y)
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s,  x + (tileSize-10 - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize-10 - (asc + desc)) / 2));
    }

    public void readFile(String file){
        String filePath = "../test/"+ file;
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(filePath)));
            int i =0;
            while(sc.hasNextLine()){
                while (i<tiles.length) {
                    String[] line = sc.nextLine().trim().split(" ");
                    for (int j=0; j<line.length; j++) {
                        tiles[i] = Integer.parseInt(line[j]);
                        i++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("tidak ada file dengan nama " + file);
        }
        blankPos=findBlankPos(tiles);
        if (!isSolvable()){
            repaint();
            System.out.println("Puzzle tidak dapat diselesaikan");
            try {
                TimeUnit.SECONDS.sleep(3);
            }
            catch(InterruptedException ex) {
            }
            setDefault();
        }
        repaint();
    }
    // function solution
    public void solution(){
        // menampung command sementara
        long nano_startTime = System.nanoTime();
        Vector<String> tempCommand = new Vector<>();
        solutionCommand.clear();
        pq.clear();
        int compare =0;
        //menampung cost
        int tempCost;
        while(!isSolve(tiles)){
            tempTiles = this.tiles.clone();
            blankPos = findBlankPos(tempTiles);
            // jika pq empty
            if(pq.isEmpty()){
                if (boolUp()){
                    tempCommand.add("up");
                    commandTemp("up");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("down");
                    tempCommand.clear();
                    compare++;
                }
                if(boolDown()){
                    tempCommand.add("down");
                    commandTemp("down");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("up");
                    tempCommand.clear();
                    compare++;
                }
                if(boolRight()){
                    tempCommand.add("right");
                    commandTemp("right");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("left");
                    tempCommand.clear();
                    compare++;
                }
                if(boolLeft()){
                    tempCommand.add("left");
                    commandTemp("left");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("right");
                    tempCommand.clear();
                    compare++;
                }
            }
            // jika tidak empty
            else{
                State temp = pq.poll();
                commandTemp(temp.command);
                tempCommand = new Vector<>(temp.command);
                if (boolUp() && !tempCommand.get(tempCommand.size()-1).equals("down")){
                    tempCommand.add("up");
                    commandTemp("up");
                    tempCost = cost(tempTiles)+ tempCommand.size();

                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("down");
                    tempCommand.remove(tempCommand.size()-1);
                    compare++;
                }
                if(boolDown()&&!tempCommand.get(tempCommand.size()-1).equals("up")){
                    tempCommand.add("down");
                    commandTemp("down");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("up");
                    tempCommand.remove(tempCommand.size()-1);
                    compare++;
                }
                if(boolRight()&&!tempCommand.get(tempCommand.size()-1).equals("left")){
                    tempCommand.add("right");
                    commandTemp("right");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("left");
                    tempCommand.removeElementAt(tempCommand.size()-1);
                    compare++;
                }
                if(boolLeft()&&!tempCommand.get(tempCommand.size()-1).equals("right")){
                    tempCommand.add("left");
                    commandTemp("left");
                    tempCost = cost(tempTiles)+ tempCommand.size();
                    pq.add(new State(tempCost,tempCommand));

                    if (isSolve(tempTiles)){
                        break;
                    }
                    commandTemp("right");
                    tempCommand.remove(tempCommand.size()-1);
                    compare++;
                }

            }
        }

        long nano_endTime = System.nanoTime();
        System.out.println("Time taken in nano seconds      : "
                           + (nano_endTime - nano_startTime));
        System.out.println("jumlah simpul yang dibangkitkan : " + compare);
        System.out.print("command Move :");
        for (int j = 0; j < tempCommand.size(); j++) {
            System.out.print(" " + tempCommand.get(j));
            if(j != tempCommand.size() - 1){
                System.out.print(",");
            }
        }
        System.out.println();
        // state solve
        this.solutionCommand = new Vector<>(tempCommand);
        blankPos = findBlankPos(tiles);
        for (int j=0;j<tempCommand.size();j++){
            command(tempCommand.get(j));
        }
    }

    public int findBlankPos(int[] array){
        for(int i=0;i<16;i++){
            if (array[i]==16){
                return i;
            }
        }
        return -1;
    }

    // cost command
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
    public void command(String command){
        int temp;
        // wait 1 second
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException ex) {
        }
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
//        this.updateUI();
        this.repaint();
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2D);
    }

}

