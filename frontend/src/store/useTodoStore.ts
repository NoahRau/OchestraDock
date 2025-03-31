import { create } from "zustand";

type State = {
  lists: List[];
};

export type List = {
  id: string;
  name: string;
  progress: number;
  userId: number;
};

type Action = {
  setLists: (lists: List[]) => void;
  addList: (list: List) => void;
  removeList: (id: string) => void;
  updateList: (id: string, updatedList: Partial<List>) => void;
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
}));

export default useTodoStore;
