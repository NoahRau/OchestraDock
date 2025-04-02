import { create } from "zustand";

type State = {
  lists: List[];
};

export type List = {
  id: string;
  name: string;
  progress: number;
  userId: number;
  todos: Todo[];
};

export type Todo = {
  id: string;
  name: string;
  checked: boolean;
};

type Action = {
  setLists: (lists: List[]) => void;
  addList: (list: List) => void;
  removeList: (id: string) => void;
  updateList: (id: string, updatedList: Partial<List>) => void;
  toggleTodo: (listId: string, todoId: string) => void;
  addTodo: (listId: string, todo: Todo) => void;
  // removeTodo: (listId: string, todoId: string) => void;
};

const useTodoStore = create<State & Action>((set) => ({
  lists: [],
  setLists: (lists) => set({ lists }),
  addList: (list) => set((state) => ({ lists: [...state.lists, list] })),
  removeList: (id) =>
    set((state) => ({ lists: state.lists.filter((list) => list.id !== id) })),
  updateList: (id, updatedList) =>
    set((state) => ({
      lists: state.lists.map((list) =>
        list.id === id ? { ...list, ...updatedList } : list
      ),
    })),
  toggleTodo: (listId, todoId) => {
    set((state) => ({
      lists: state.lists.map((list) => {
        if (list.id === listId) {
          return {
            ...list,
            todos: list.todos.map((todo) =>
              todo.id === todoId ? { ...todo, checked: !todo.checked } : todo
            ),
          };
        } else {
          return list;
        }
      }),
    }));
  },
  addTodo: (listId, todo) =>
    set((state) => ({
      lists: state.lists.map((list) => {
        if (list.id === listId) {
          return {
            ...list,
            todos: [...list.todos, todo],
          };
        } else {
          return list;
        }
      }),
    })),
  // fetchTodos: (listId) =>
  //   set((state) => ({
  //   }))
}));

export default useTodoStore;
