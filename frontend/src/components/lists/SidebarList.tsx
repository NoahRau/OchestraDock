import { Link } from "react-router";
import {
  SidebarMenuBadge,
  SidebarMenuButton,
  SidebarMenuItem,
} from "../ui/sidebar";
import { Skeleton } from "../ui/skeleton";
import { CircularProgressbar } from "react-circular-progressbar";
import { List } from "@/store/useTodoStore";

type Props = {
  as: "sidebar" | "div";
  data: List[];
  loading: boolean;
};

function SidebarList({ as, data, loading }: Props) {
  if (loading) {
    return (
      <>
        <SidebarMenuItem>
          <SidebarMenuButton>
            <Skeleton className="w-full h-full rounded-full" />
          </SidebarMenuButton>
        </SidebarMenuItem>
        <SidebarMenuItem>
          <SidebarMenuButton>
            <Skeleton className="w-full h-full rounded-full" />
          </SidebarMenuButton>
        </SidebarMenuItem>
        <SidebarMenuItem>
          <SidebarMenuButton>
            <Skeleton className="w-full h-full rounded-full" />
          </SidebarMenuButton>
        </SidebarMenuItem>
      </>
    );
  }

  if (as === "sidebar") {
    return (
      <>
        {data.map((item) => {
          return (
            <SidebarMenuItem key={item.id}>
              <SidebarMenuButton>
                <Link to={`/lists/${item.id}`}>{item.name}</Link>
              </SidebarMenuButton>
              <SidebarMenuBadge>
                <CircularProgressbar
                  value={item.progress * 100}
                  className="size-4 strok-red-500"
                  background={false}
                  strokeWidth={20}
                />
              </SidebarMenuBadge>
            </SidebarMenuItem>
          );
        })}
      </>
    );
  }
  return (
    <>
      {data.map((item) => {
        return (
          <div key={item.id}>
            <Link to={`/lists/${item.id}`}>{item.name}</Link>
          </div>
        );
      })}
    </>
  );
}

export default SidebarList;
