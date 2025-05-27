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
      buildInputs = with pkgs; [
        jdk21
        kotlin
        maven
        nodejs_23
        jq
        ktlint

        minikube
        kubernetes-helm
        kubectl
        k9s
      ];
    };
  };
}
