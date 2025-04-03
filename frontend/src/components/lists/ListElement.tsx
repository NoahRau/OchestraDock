import { Checkbox } from "../ui/checkbox";
import clsx from "clsx";
import { Button } from "../ui/button";
import { Trash2 } from "lucide-react";
import List from "@/pages/List";

function ListElement({
  data,
  handleDelete,
  toggleTodo,
}: {
  data: List[];
  handleDelete: (id: string) => void;
  toggleTodo: (id: List["id"]) => void;
}) {
  return (
    <div className="space-y-4">
      {data.map((todoData: List) => (
        <ListElementItem
          key={todoData.id}
          handleDelete={handleDelete}
          todoData={todoData}
          toggleTodo={toggleTodo}
        />
      ))}
    </div>
  );
}

function ListElementItem({
  todoData,
  handleDelete,
  toggleTodo,
}: {
  todoData: List;
  handleDelete: (id: string) => void;
  toggleTodo: (id: List["id"]) => void;
}) {
  const deleteTodo = () => {
    handleDelete(todoData.id);
  };

  return (
    <div
      className="flex items-center group cursor-pointer"
      onClick={() => toggleTodo(todoData.id)}
    >
      <div className="flex items-center">
        <Checkbox
          id={todoData.id}
          checked={todoData.completed}
          className="size-5"
        />
        <label
          htmlFor={todoData.id}
          className={clsx(
            "ml-2 pointer-events-none group-hover:font-semibold transition-all",
            todoData.completed && "line-through"
          )}
        >
          {todoData.description}
        </label>
      </div>
      <Button
        variant={"ghost"}
        onClick={deleteTodo}
        className="ml-auto opacity-0 group-hover:opacity-100 cursor-pointer"
      >
        <Trash2 />
      </Button>
    </div>
  );
}

export default ListElement;
