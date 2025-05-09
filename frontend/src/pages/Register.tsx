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
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import { useAuthStore } from "@/store/useAuthStore.ts";
import axios from "axios";
import ErrorAlert from "@/components/ErrorAlert";

function Register() {
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [password2, setPassword2] = useState<string>("");

  const [err, setErr] = useState<boolean>(false);

  const { accessToken, login } = useAuthStore();
  const navigate = useNavigate();

  useEffect(() => {
    if (accessToken) {
      navigate("/", { replace: true });
    }
  }, [accessToken, navigate]);

  const handleRegister = async () => {
    if (password !== password2) {
      alert("Passwörter stimmen nicht überein.");
      return;
    }

    try {
      const res = await axios.post(
        "http://localhost:3080/api/v1/auth/register",
        {
          username,
          password,
        }
      );

      const { token } = res.data;
      login(token);
      navigate("/login");
    } catch (err) {
      console.error("Registrierung fehlgeschlagen:", err);
      setErr(true);
    }
  };

  return (
    <main className="h-svh grid place-items-center bg-[url(/sheng.avif)] bg-cover">
      <Card className="w-md">
        <CardHeader>
          <CardTitle>TODO-APP</CardTitle>
          <CardDescription>
            Du hast bereits einen Account?{" "}
            <Link to={"/login"} className="text-orange-600">
              Login
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
          <LabeledInput
            label="Passwort wiederholen"
            id="password2"
            placeholder="Passwort"
            type="password"
            input={{
              value: password2,
              onChange: (e) => setPassword2(e.target.value),
            }}
          />
          {err && (
            <ErrorAlert
              title="Fehler"
              description="Fehler bei der Registrierung."
            />
          )}
        </CardContent>
        <CardFooter>
          <Button
            className="cursor-pointer bg-orange-600 hover:bg-orange-700 text-white"
            onClick={handleRegister}
            disabled={!username || !password || !password2}
          >
            Registrieren
          </Button>
        </CardFooter>
      </Card>
    </main>
  );
}

export default Register;
