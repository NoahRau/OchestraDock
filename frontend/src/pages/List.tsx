import ListElement from "@/components/lists/ListElement";
import { Button } from "@/components/ui/button";
import useTodoStore from "@/store/useTodoStore";
import { Trash2 } from "lucide-react";
import { useParams } from "react-router";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import AddTodo from "@/components/lists/AddTodo";
import { useEffect } from "react";
import api from "@/api/api";

function List() {
  // const addTodo = useTodoStore((state) => state.addTodo);
  const { id } = useParams();

  const list = useTodoStore((state) =>
    state.lists.find((list) => list.id === id)
  );

  const handleCreate = async (name: string) => {
    console.log(name);
  };

  const handleDelete = async () => {};

  useEffect(() => {
    const fetchTodos = async () => {
      const res = await api.get("/tasks");
      console.log(res);
    };
    fetchTodos();
  }, []);

  if (!list) {
    return <div className="flex flex-col justify-center h-full"></div>;
  }

  return (
    <div className="flex flex-col justify-center h-full">
      <div className="flex justify-between items-center">
        <h1 className="text-7xl font-black mb-4">{list?.name}</h1>
        <div className="">
          <AlertDialog>
            <AlertDialogTrigger asChild>
              <Button variant="ghost" className="cursor-pointer">
                <span className="sr-only">Löschen</span>
                <Trash2 className="size-6 stroke-orange-600" />
              </Button>
            </AlertDialogTrigger>
            <AlertDialogContent>
              <AlertDialogHeader>
                <AlertDialogTitle>Bist du dir sicher?</AlertDialogTitle>
                <AlertDialogDescription>
                  Du wirst diese Liste und alle ihre Todos löschen. Dies kann
                  nicht rückgängig gemacht werden. Bist du dir sicher, dass du
                  fortfahren möchtest?
                </AlertDialogDescription>
              </AlertDialogHeader>
              <AlertDialogFooter>
                <AlertDialogCancel>Abbrechen</AlertDialogCancel>
                <AlertDialogAction onClick={handleDelete}>
                  Löschen
                </AlertDialogAction>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
          <AddTodo create={handleCreate} />
        </div>
      </div>
      <ListElement data={list} />
    </div>
  );
}

export default List;
