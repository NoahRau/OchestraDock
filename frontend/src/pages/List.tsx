import ListElement from "@/components/lists/ListElement";
import AddTodo from "@/components/lists/AddTodo";
import { useEffect, useLayoutEffect, useState } from "react";
import api from "@/api/api";
import axios from "axios";
import { Link } from "react-router";

type List = {
  completed: boolean;
  createdAt: string;
  id: string;
  description: string;
  project: string;
  updatedAt: string;
  userId: string;
};

function List() {
  const [list, setList] = useState<List[]>([]);
  const [image, setImage] = useState<{
    date: string;
    url: string;
    author: string;
    description: string;
  } | null>(null);

  const handleDelete = async (id: string) => {
    await api.delete(`/tasks/${id}`);
    setList((prev) => prev.filter((item) => item.id !== id));
  };

  const toggleTodo = async (id: List["id"]) => {
    const todo = list.find((item) => item.id === id);
    if (!todo) return;

    const updatedTodo = { ...todo, completed: !todo.completed };
    await api.put(`/tasks/${id}`, updatedTodo);
    setList((prev) =>
      prev.map((item) => (item.id === id ? updatedTodo : item))
    );
  };

  const handleCreate = async (name: string) => {
    const res = await api.post("/tasks", {
      description: name,
      completed: false,
      project: "",
    });
    setList((prev) => [...prev, res.data]);
  };

  useEffect(() => {
    const fetchTodos = async () => {
      const res = await api.get("/tasks");
      setList(res.data as List[]);
    };
    fetchTodos();
  }, []);

  useLayoutEffect(() => {
    const fetchImage = async () => {
      const res = await axios.get("http://localhost:8000/image/today");
      const img = res.data as {
        date: string;
        url: string;
        author: string;
        description: string;
      };
      setImage(img);
    };
    fetchImage();
  }, []);

  if (!list) {
    return <div className="flex flex-col justify-center h-full"></div>;
  }

  return (
    <div className="h-svh grid grid-cols-2">
      <div className="flex flex-col justify-center px-4 md:px-8">
        <div className="flex justify-between items-center">
          <h1 className="text-7xl font-black mb-4">Todos</h1>
          <div className="">
            <AddTodo create={handleCreate} />
          </div>
        </div>
        <ListElement
          data={list}
          toggleTodo={toggleTodo}
          handleDelete={handleDelete}
        />
      </div>
      <div className="relative">
        <img
          src={image?.url}
          alt={image?.description}
          className="h-full object-cover"
        />
        <div className="bg-white absolute py-0.5 px-2 rounded-tl right-0 bottom-0">
          {image?.author}
        </div>
      </div>
    </div>
  );
}

export default List;
