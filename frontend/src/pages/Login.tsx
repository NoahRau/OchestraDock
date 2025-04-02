import ErrorAlert from "@/components/ErrorAlert";
import LabeledInput from "@/components/LabeledInput";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { useAuthStore } from "@/store/useAuthStore";
import axios from "axios";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";

function Login() {
  const [err, setErr] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { accessToken, login } = useAuthStore();
  const navigate = useNavigate();

  useEffect(() => {
    if (accessToken) {
      navigate("/", { replace: true });
    }
  }, [accessToken, navigate]);

  const handleLogin = async () => {
    try {
      const res = await axios.post("http://localhost:3080/api/v1/auth/login", {
        username,
        password,
      });
      console.log(res);
      const { token } = res.data;
      login(token);
      navigate("/");
    } catch (err) {
      console.error("Login fehlgeschlagen:", err);
      setErr(true);
    }
  };
  return (
    <main className="h-svh grid place-items-center bg-[url(/peter.avif)] bg-cover bg-center">
      <Card className="w-md">
        <CardHeader>
          <CardTitle>TODO-APP</CardTitle>
          <CardDescription>
            Bitte Einloggen! Noch keinen Account?{" "}
            <Link to={"/register"} className="text-orange-600">
              Register
            </Link>
          </CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4">
          <LabeledInput
            label="Dein Username"
            id="username"
            placeholder="Username"
            type="text"
            input={{
              value: username,
              onChange: (e) => setUsername(e.target.value),
            }}
          />
          <LabeledInput
            label="Passwort"
            id="password"
            placeholder="Passwort"
            type="password"
            input={{
              value: password,
              onChange: (e) => setPassword(e.target.value),
            }}
          />
          {err && (
            <ErrorAlert
              title="Fehler"
              description="Falscher User oder Passwort."
            />
          )}
        </CardContent>
        <CardFooter>
          <Button
            type="submit"
            className="cursor-pointer bg-orange-600 hover:bg-orange-700 text-white"
            onClick={handleLogin}
            disabled={!username || !password}
          >
            Login
          </Button>
        </CardFooter>
      </Card>
    </main>
  );
}

export default Login;
