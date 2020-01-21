import java.util.*;

public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2

	private List<List<positionTicTacToe>>  winningLines = initializeWinningLines();

	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board,int i) {
		long start_time = System.currentTimeMillis();

		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);
		List<positionTicTacToe> newBoard = deepCopyATicTacToeBoard(board);

		int[] result = minimax(newBoard, 3,Integer.MIN_VALUE, Integer.MAX_VALUE,this.player);

		myNextMove.x=result[1];
		myNextMove.y=result[2];
		myNextMove.z=result[3];
		long end_time = System.currentTimeMillis();
		long time = (end_time-start_time)/1000;
		System.out.println(time);
		System.out.println(myNextMove.x+" " + myNextMove.y+" "+myNextMove.z);
		return myNextMove;
	}

	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}

	private int[] minimax(List<positionTicTacToe> board,int depth, int alpha, int beta,int player) {
		List<int[]> nextMoves = this.generateMoves(board);

		int score;
		int bestX = -1;
		int bestY = -1;
		int bestZ = -1;

		if (isEnded(board) !=0 || depth == 0) {
			// Gameover or depth reached, evaluate score
			score = evaluate(board);
		}else{
			if (player == this.player){
				return getMax(board,depth-1,alpha,beta,player);
			}else{
				return getMin(board,depth-1,alpha,beta,player);
			}
		}
		return new int[]{score, bestX, bestY, bestZ};
	}
	/*
        else {
        for (int[] move : nextMoves) {
            positionTicTacToe position = new positionTicTacToe(move[0],move[1],move[2]);
            board = makeMove(position,player,board);
            if (player == this.player) {
                score = minimax(board,depth - 1,alpha, beta,3- this.player)[0];
                if (score > alpha) {
                    alpha = score;
                    bestX = move[0];
                    bestY = move[1];
                    bestZ = move[2];
                }
            } else {
                score = minimax(board,depth - 1,alpha, beta,this.player)[0];
                if (score < beta) {
                    beta = score;
                    bestX = move[0];
                    bestY = move[1];
                    bestZ = move[2];
                }
            }
            if (alpha >= beta) break;

        }
        return new int[] {(player == this.player) ? alpha : beta, bestX, bestY,bestZ};
    }
}
*/
	private int[] getMax (List<positionTicTacToe> board,int depth, int alpha, int beta,int player) {
		List<int[]> nextMoves = this.generateMoves(board);


		int score;
		int bestX = -1;
		int bestY = -1;
		int bestZ = -1;


		for (int[] move : nextMoves) {
			positionTicTacToe position = new positionTicTacToe(move[0],move[1],move[2]);
			List<positionTicTacToe> newBoard = deepCopyATicTacToeBoard(board);
			newBoard = makeMove(position,player,newBoard);

			score = minimax(newBoard,depth,alpha,beta,3-player)[0];

			if (score > alpha) {
				alpha = score;
				bestX = move[0];
				bestY = move[1];
				bestZ = move[2];
			}

			if (alpha >= beta) {
				break;
			}
		}

		return new int[] {alpha, bestX, bestY,bestZ};
	}


	private int[] getMin (List<positionTicTacToe> board,int depth, int alpha, int beta,int player) {
		List<int[]> nextMoves = this.generateMoves(board);

		int score;
		int bestX = -1;
		int bestY = -1;
		int bestZ = -1;


		for (int[] move : nextMoves) {
			positionTicTacToe position = new positionTicTacToe(move[0],move[1],move[2]);
			List<positionTicTacToe> newBoard = deepCopyATicTacToeBoard(board);
			newBoard = makeMove(position,player,newBoard);

			score = minimax(newBoard,depth,alpha,beta,3-player)[0];

			if (score < beta) {
				beta = score;
				bestX = move[0];
				bestY = move[1];
				bestZ = move[2];
			}

			if (alpha >= beta) {
				break;
			}
		}

		return new int[] {beta, bestX, bestY,bestZ};
	}


	private int evaluate(List<positionTicTacToe> board) {
		int score = 0;
		// Evaluate score for each of the 76 lines (3 rows, 3 columns, 2 diagonals)
		// Layer-wise(change z),4*10
		for(int z=0;z<4;z++){
			//row1-row4
			for(int x=0;x<4;x++){
				score += evaluateLine(x,x,x,x,0,1,2,3,z,z,z,z,board);
			}
			//col1-col4
			for(int y=0;y<4;y++){
				score += evaluateLine(0,1,2,3,y,y,y,y,z,z,z,z,board);
			}
			// two diagnols
			score += evaluateLine(0,1,2,3,0,1,2,3,z,z,z,z,board);
			score += evaluateLine(0,1,2,3,3,2,1,0,z,z,z,z,board);
		}

		// Four cubic wise diagnols
		score += evaluateLine(0,1,2,3,0,1,2,3,0,1,2,3,board);
		score += evaluateLine(0,1,2,3,3,2,1,0,0,1,2,3,board);
		score += evaluateLine(3,2,1,0,0,1,2,3,0,1,2,3,board);
		score += evaluateLine(3,2,1,0,3,2,1,0,0,1,2,3,board);

		// // Layer-wise(change y),4*8
		for(int y=0;y<4;y++){
			//col1-col4
			for(int x=0;x<4;x++){
				score += evaluateLine(x,x,x,x,y,y,y,y,0,1,2,3,board);
			}
			//rol2,ro3
			score += evaluateLine(0,1,2,3,y,y,y,y,1,1,1,1,board);
			score += evaluateLine(0,1,2,3,y,y,y,y,2,2,2,2,board);
			// two diagnols
			score += evaluateLine(0,1,2,3,y,y,y,y,0,1,2,3,board);
			score += evaluateLine(3,2,1,0,y,y,y,y,0,1,2,3,board);
		}

		return score;
	}

	private int evaluateLine(int x1,int x2, int x3,int x4, int y1,int y2, int y3,int y4, int z1, int z2,int z3,int z4, List<positionTicTacToe> board) {
		int score = 0;
		positionTicTacToe position1 = new positionTicTacToe(x1,y1,z1);
		positionTicTacToe position2 = new positionTicTacToe(x2,y2,z2);
		positionTicTacToe position3 = new positionTicTacToe(x3,y3,z3);
		positionTicTacToe position4 = new positionTicTacToe(x4,y4,z4);

		// First cell
		if (getStateOfPositionFromBoard(position1,board)== this.player) {
			score = 1;
		} else if (getStateOfPositionFromBoard(position1,board)== 3 - this.player) {
			score = -1;
		}

		// Second cell
		if (getStateOfPositionFromBoard(position2,board)== this.player) {
			if (score == 1) {   // 1st cell1 is mySeed
				score = 100;
			} else if (score == -1) {  // cell1 is oppSeed
				return 0;
			} else {  // cell1 is empty
				score = 1;
			}

		} else if (getStateOfPositionFromBoard(position2,board)== 3 - this.player) {
			if (score == -1) { // cell1 is oppSeed
				score = -100;
			} else if (score == 1) { // cell1 is mySeed
				return 0;
			} else {  // cell1 is empty
				score = -1;
			}
		}

		// Third cell
		if (getStateOfPositionFromBoard(position3,board)== this.player) {
			if (score > 0) {  // cell1 and/or cell2 is mySeed
				score *= 100;
			} else if (score < 0) {  // cell1 and/or cell2 is oppSeed
				return 0;
			} else {  // cell1 and cell2 are empty
				score = 1;
			}
		} else if (getStateOfPositionFromBoard(position3,board)== 3 - this.player) {
			if (score < 0) {  // cell1 and/or cell2 is oppSeed
				score *= 100;
			} else if (score > 1) {  // cell1 and/or cell2 is mySeed
				return 0;
			} else {  // cell1 and cell2 are empty
				score = -1;
			}
		}

		if (getStateOfPositionFromBoard(position4,board)== this.player) {
			if (score > 0) {  // cell1 and/or cell2 is mySeed
				score *= 100;
			} else if (score < 0) {  // cell1 and/or cell2 is oppSeed
				return 0;
			} else {  // cell1 and cell2 are empty
				score = 1;
			}
		} else if (getStateOfPositionFromBoard(position4,board)== 3 - this.player) {
			if (score < 0) {  // cell1 and/or cell2 is oppSeed
				score *= 100;
			} else if (score > 0) {  // cell1 and/or cell2 is mySeed
				return 0;
			} else {  // cell1 and cell2 are empty
				score = -1;
			}
		}

		return score;
	}


	/** Minimax (recursive) at level of depth for maximizing or minimizing player
	 with alpha-beta cut-off. Return int[3] of {score, x, y, z}  */


	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any 	winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();

		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}

		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
			oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
			oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
			oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
			winningLines.add(oneWinCondtion);
		}
		//yz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
			oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
			oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
			oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
			winningLines.add(oneWinCondtion);
		}
		//xy plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
			oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
			oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
			oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
			winningLines.add(oneWinCondtion);
		}

		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
			oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
			oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
			oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
			winningLines.add(oneWinCondtion);
		}
		//yz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
			oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
			oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
			oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
			winningLines.add(oneWinCondtion);
		}
		//xy plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
			oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
			oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
			oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
			winningLines.add(oneWinCondtion);
		}

		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);

		return winningLines;
	}

	private int isEnded(List<positionTicTacToe> board)
	{
		//test whether the current game is ended

		//brute-force
		for(int i=0;i<winningLines.size();i++)
		{

			positionTicTacToe p0 = winningLines.get(i).get(0);
			positionTicTacToe p1 = winningLines.get(i).get(1);
			positionTicTacToe p2 = winningLines.get(i).get(2);
			positionTicTacToe p3 = winningLines.get(i).get(3);

			int state0 = getStateOfPositionFromBoard(p0,board);
			int state1 = getStateOfPositionFromBoard(p1,board);
			int state2 = getStateOfPositionFromBoard(p2,board);
			int state3 = getStateOfPositionFromBoard(p3,board);

			//if they have the same state (marked by same player) and they are not all marked.
			if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
			{
				//someone wins
				p0.state = state0;
				p1.state = state1;
				p2.state = state2;
				p3.state = state3;

				return state0;
			}
		}
		for(int i=0;i<board.size();i++)
		{
			if(board.get(i).state==0)
			{
				//game is not ended, continue
				return 0;
			}
		}
		return -1; //call it a draw
	}


	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
	}

	public List<int[]> generateMoves(List<positionTicTacToe> board) {
		List<int[]> nextMoves = new ArrayList<int[]>();
		if (isEnded(board) != 0) {
			return nextMoves;   // return empty list
		}
		positionTicTacToe aNewMove;

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int z = 0; z < 4; z++) {
					aNewMove = new positionTicTacToe(x,y,z);
					if (getStateOfPositionFromBoard(aNewMove,board)==0) {
						nextMoves.add(new int[] {x,y,z});
					}
				}
			}
			return nextMoves;
		}
		return nextMoves;
	}

	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
	{
		//deep copy of game boards
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for(int i=0;i<board.size();i++)
		{
			copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
		}
		return copiedBoard;
	}

	private List<positionTicTacToe> makeMove(positionTicTacToe position, int player, List<positionTicTacToe> targetBoard) {
		//make move on Tic-Tac-Toe board, given position and player
		//player 1 = 1, player 2 = 2

		//brute force (obviously not a wise way though)
		for (int i = 0; i < targetBoard.size(); i++) {
			if (targetBoard.get(i).x == position.x && targetBoard.get(i).y == position.y && targetBoard.get(i).z == position.z) //if this is the position
			{
				if (targetBoard.get(i).state == 0) {
					targetBoard.get(i).state = player;
				} else {
					System.out.println("Error: this is not a valid move.");
				}
			}
		}
		return targetBoard;
	}

	private List<positionTicTacToe> undoMove(positionTicTacToe position, List<positionTicTacToe> targetBoard) {
		//make move on Tic-Tac-Toe board, given position and player
		//player 1 = 1, player 2 = 2

		//brute force (obviously not a wise way though)
		for (int i = 0; i < targetBoard.size(); i++) {
			if (targetBoard.get(i).x == position.x && targetBoard.get(i).y == position.y && targetBoard.get(i).z == position.z) //if this is the position
			{
				targetBoard.get(i).state = 0;
			}
		}
		return targetBoard;
	}
}

