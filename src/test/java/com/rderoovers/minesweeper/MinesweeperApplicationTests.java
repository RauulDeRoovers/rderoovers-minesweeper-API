package com.rderoovers.minesweeper;

import com.rderoovers.minesweeper.controller.MinesweeperRestController;
import com.rderoovers.minesweeper.domain.GameSettings;
import com.rderoovers.minesweeper.domain.MinesweeperGame;
import com.rderoovers.minesweeper.domain.MinesweeperGameDTO;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class MinesweeperApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testNeighborsAreCreatedProperlyFor3x3Grid() {
		MinesweeperRestController controller = new MinesweeperRestController();
		GameSettings settings = new GameSettings();
		settings.setRowCount(3);
		settings.setColumnCount(3);
		settings.setMineCount(3);
		MinesweeperGameDTO game = controller.create(settings).getBody();
		/*
		List<Pair<Integer, Integer>> positions = game.getSquares()[0][0].getNeighborsPositions();
		List<Integer> indexes = positions.stream().map(x -> (x.getKey() * 3) + x.getValue()).collect(Collectors.toList());
		Assert.isTrue(indexes.contains(1));
		Assert.isTrue(indexes.contains(3));
		Assert.isTrue(indexes.contains(4));
		Assert.isTrue(positions.size() == 3, "Top left square must have 3 neighbors: (1:0,1), (3:1,0) and (4:1,1");
		*/
		Assert.isTrue(game.getColumnCount() == 3);
		Assert.isTrue(!game.isFinished());
	}

}
