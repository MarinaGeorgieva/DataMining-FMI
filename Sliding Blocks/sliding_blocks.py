import math
import heapq


class State:
    def __init__(self, board):
        self.board = board
        self.f = 0
        self.g = 0
        self.parent = None
        self.direction = None

    def __le__(self, other):
        return self.f <= other.f

    def __eq__(self, other):
        return self.board == other.board

    def __hash__(self):
        return hash(self.board)


class Puzzle:
    def __init__(self, n, initial):
        self.initial = State(initial)
        self.goal = range(1, n + 1)
        self.goal.append(0)
        self.size = int(math.sqrt(n + 1))
        self.all = set()

    def find_solution(self):
        explored = set()
        unexplored = []
        heapq.heapify(unexplored)

        self.initial.g = 0
        self.initial.f = self.heuristic(self.initial)
        heapq.heappush(unexplored, self.initial)

        while unexplored:
            current_state = heapq.heappop(unexplored)

            if current_state.board == tuple(self.goal):
                result = self.result(current_state)
                return str(result[0]) + '\n' + str(result[1])

            explored.add(current_state)
            next_states = self.next_states(current_state)
            for child_state in next_states:
                if child_state in explored:
                    continue

                child_state.g = current_state.g + 1
                child_state.f = child_state.g + self.heuristic(child_state)
                child_state.parent = current_state
                heapq.heappush(unexplored, child_state)

    def result(self, current_state):
        path = [current_state.direction]
        while current_state.parent:
            current_state = current_state.parent
            path.append(current_state.direction)
        path.reverse()
        del path[0]
        return len(path), path

    def next_states(self, state):
        moves = [('left', -1),
                 ('right', 1),
                 ('up', -self.size),
                 ('down', self.size)]

        zero_pos = state.board.index(0)
        zero_x, zero_y = self.__coordinates(0, state.board)

        for direction, delta in moves:
            target_pos = zero_pos - delta

            # check if target position is inside boundries
            if target_pos < 0 or target_pos >= len(state.board):
                continue

            target = state.board[target_pos]
            target_x, target_y = self.__coordinates(target, state.board)

            if target_x == 0 and direction == 'up':
                continue

            if target_x == self.size - 1 and direction == 'down':
                continue

            if target_y == 0 and direction == 'left':
                continue

            if target_y == self.size - 1 and direction == 'right':
                continue

            # make the move
            new_board = self.__move(zero_pos, target_pos, state)
            next_state = State(new_board)
            next_state.direction = direction

            if next_state not in self.all:
                self.all.add(next_state)
                yield next_state

    def heuristic(self, state):
        sum = 0
        for block in self.goal:
            x, y = self.__coordinates(block, state.board)
            target_x, target_y = self.__coordinates(block, self.goal)
            sum += self.__manhattan_distance(x, y, target_x, target_y)
        return sum

    def __manhattan_distance(self, x1, y1, x2, y2):
        return abs(x1 - x2) + abs(y1 - y2)

    def __coordinates(self, block, board):
        x = board.index(block) // self.size
        y = board.index(block) % self.size
        return x, y

    def __move(self, i, j, state):
        board = list(state.board)
        board[i], board[j] = board[j], board[i]
        return tuple(board)


# # 22 steps
# puzzle = Puzzle(8, (0, 3, 2, 1, 7, 8, 5, 4, 6))
# print(puzzle.find_solution())

# # 28 steps
# puzzle = Puzzle(8, (8, 2, 7, 3, 5, 4, 6, 1, 0))
# print(puzzle.find_solution())

# # 30 steps
# puzzle = Puzzle(8, (8, 7, 6, 5, 4, 3, 2, 1, 0))
# print(puzzle.find_solution())

# # 21 steps
# puzzle = Puzzle(8, (6, 5, 3, 2, 4, 8, 7, 0, 1))
# print(puzzle.find_solution())

# 8 steps
puzzle = Puzzle(8, (2, 3, 6, 1, 5, 8, 4, 7, 0))
print(puzzle.find_solution())

# # 2 steps
# puzzle = Puzzle(8, (1, 2, 3, 4, 5, 6, 0, 7, 8))
# print(puzzle.find_solution())

# # 4 steps
# puzzle = Puzzle(8, (0, 1, 3, 4, 2, 5, 7, 8, 6))
# print(puzzle.find_solution())

# # 18 steps
# puzzle = Puzzle(8, (4, 1, 2, 8, 0, 3, 6, 5, 7))
# print(puzzle.find_solution())

# # 24 steps
# puzzle = Puzzle(8, (5, 7, 8, 6, 0, 4, 1, 3, 2))
# print(puzzle.find_solution())

# # 3 steps
# puzzle = Puzzle(15, (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 13, 14, 15))
# print(puzzle.find_solution())

# # 4 steps
# puzzle = Puzzle(15, (1, 2, 3, 4, 5, 6, 7, 8, 0, 9, 10, 11, 13, 14, 15, 12))
# print(puzzle.find_solution())
