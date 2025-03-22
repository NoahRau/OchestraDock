{
  description = "devShell";

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
        pkgs.jdk21
        pkgs.kotlin
        pkgs.maven
        pkgs.nodejs_23
        pkgs.jq
      ];
    };
  };
}
