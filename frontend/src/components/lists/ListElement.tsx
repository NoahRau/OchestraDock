import useTodoStore, { List, Todo } from "@/store/useTodoStore";
import { Checkbox } from "../ui/checkbox";
import clsx from "clsx";
import { Button } from "../ui/button";
import { Trash2 } from "lucide-react";

function ListElement({ data }: { data: List }) {
  // const fetchTodos = useTodoStore((state) => state.fetchTodos);

  // useEffect(() => {
  //   fetchTodos(data.id);
  // }, [data.id, fetchTodos]);
  return (
    <div className="space-y-4">
      {data.todos.map((todoData) => (
        <ListElementItem
          key={todoData.id}
          todoData={todoData}
          listid={data.id}
        />
      ))}
    </div>
  );
}

function ListElementItem({
  todoData,
  listid,
}: {
  todoData: Todo;
  listid: string;
}) {
  const toggleTodo = useTodoStore((state) => state.toggleTodo);

  return (
    <div
      className="flex items-center group cursor-pointer "
      onClick={() => toggleTodo(listid, todoData.id)}
    >
      <div className="flex items-center">
        <Checkbox
          id={todoData.id}
          checked={todoData.checked}
          className="size-5"
        />
        <label
          htmlFor={todoData.id}
          className={clsx(
            "ml-2 pointer-events-none group-hover:font-semibold transition-all",
            todoData.checked && "line-through"
          )}
        >
          {todoData.name}
        </label>
      </div>
      <Button
        variant={"ghost"}
        className="ml-auto opacity-0 group-hover:opacity-100"
      >
        <Trash2 />
      </Button>
    </div>
  );
}

export default ListElement;
