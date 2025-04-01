import { ChevronUp, Home, Plus, User2 } from "lucide-react";

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupAction,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { useEffect, useState } from "react";
import { Link } from "react-router";
import SidebarList from "./lists/SidebarList";
import useTodoStore from "@/store/useTodoStore";

// Menu items.
const items = [
  {
    title: "Home",
    url: "/",
    icon: Home,
  },
];

export function AppSidebar() {
  const [loading, setLoading] = useState<boolean>(false);

  const lists = useTodoStore((state) => state.lists);
  const setLists = useTodoStore((state) => state.setLists);

  useEffect(() => {
    setLoading(true);
    const dummy = [
      { id: "1", name: "Projekt A", progress: 0.45, userId: 1 },
      { id: "2", name: "Projekt B", progress: 0.8, userId: 1 },
      { id: "3", name: "Projekt C", progress: 0.2, userId: 1 },
      { id: "4", name: "Projekt D", progress: 1.0, userId: 1 },
    ];
    setTimeout(() => {
      setLists(dummy);
      setLoading(false);
    }, 2000);
  }, []);
  return (
    <Sidebar variant="inset" className="h-svh">
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Menu</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <Link to={item.url}>
                      <item.icon />
                      <span>{item.title}</span>
                    </Link>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        <SidebarGroup>
          <SidebarGroupLabel>Todo Listen</SidebarGroupLabel>
          <SidebarGroupAction title="Liste erstellen">
            <Plus /> <span className="sr-only">Neue Liste anlegen</span>
          </SidebarGroupAction>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarList as="sidebar" data={lists} loading={loading} />
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <SidebarMenu>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <SidebarMenuButton>
                <User2 /> Username
                <ChevronUp className="ml-auto" />
              </SidebarMenuButton>
            </DropdownMenuTrigger>
            <DropdownMenuContent
              side="top"
              className="w-[--radix-popper-anchor-width]"
            >
              <DropdownMenuItem>
                <span>Account</span>
              </DropdownMenuItem>
              <DropdownMenuItem>
                <span>Sign out</span>
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </SidebarMenu>
      </SidebarFooter>
    </Sidebar>
  );
}
