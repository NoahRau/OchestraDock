{
  description = "Nix flake devShell for the cloud-native Todo App with Python dependencies";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";

  outputs = {
    self,
    nixpkgs,
    ...
  }: let
    system = "x86_64-linux";
    pkgs = import nixpkgs {inherit system;};
  in {
    devShells.${system}.default = pkgs.mkShell {
      buildInputs = [
        pkgs.java21
        pkgs.maven
      ];
      shellHook = ''
        alias build="mvn clean install"
      '';
    };
  };
}
