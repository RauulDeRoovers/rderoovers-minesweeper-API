var minesweeperService = function($http) {
	return {
		createNewGame: function(rows, cols, mines) {
			let data = JSON.stringify({
				rowCount: rows,
				columnCount: cols,
				mineCount: mines
			});
			//return $http.post('http://localhost:8080/create', data);
			return $http.post('https://minesweeper-rest-service.herokuapp.com/create', data);
		},
		clickSquare: function(game, index) {
			let data = JSON.stringify({
				id: game.id,
				time: game.playTime,
				square: index
			});
			//return $http.patch('http://localhost:8080/update', data);
			return $http.patch('https://minesweeper-rest-service.herokuapp.com/update', data);
		},
		flagSquare: function(game, index) {
			let data = JSON.stringify({
				id: game.id,
				time: game.playTime,
				square: index
			});
			//return $http.patch('http://localhost:8080/flag', data);
			return $http.patch('https://minesweeper-rest-service.herokuapp.com/flag', data);
		},
		loadGame: function(id) {
			//return $http.get('http://localhost:8080/get?id='+ id);
			return $http.get('https://minesweeper-rest-service.herokuapp.com/get?id='+ id);
		},
		loadAllGames: function() {
			//return $http.get('http://localhost:8080/get');
			return $http.get('https://minesweeper-rest-service.herokuapp.com/get');
		},
		deleteGame: function(id) {
			//return $http.delete('http://localhost:8080/delete?id=' + id);
			return $http.delete('https://minesweeper-rest-service.herokuapp.com/delete?id=' + id);
		},
		signIn: function(user) {
			let data = JSON.stringify({
				user: user.user,
				password: user.password
			});
			//return $http.post('http://localhost:8080/signin', data);
			return $http.delete('https://minesweeper-rest-service.herokuapp.com/signin', data);
		}
	}
};

var app = angular.module('minesweeperServiceModule', []);
app.service('MinesweeperService', minesweeperService);