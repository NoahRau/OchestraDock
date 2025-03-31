import { Input } from "./ui/input";
import { Label } from "./ui/label";

type Props = {
  label: string;
  type: "text" | "email" | "password";
  id: string;
  placeholder: string;
  input?: React.ComponentPropsWithoutRef<"input">;
  labelProps?: React.ComponentPropsWithoutRef<"label">;
};

function LabeledInput({
  label,
  type,
  id,
  placeholder,
  input,
  labelProps,
}: Props) {
  return (
    <div className="grid w-full items-center gap-1.5">
      <Label htmlFor={id} {...labelProps}>
        {label}
      </Label>
      <Input type={type} id={id} placeholder={placeholder} {...input} />
    </div>
  );
}

export default LabeledInput;
