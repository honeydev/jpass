{
  description = "Spring Boot Java 25 development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
        };

        jdk = pkgs.jdk25;
      in {
        devShells.default = pkgs.mkShell {
          packages = with pkgs; [
            jdk
            gradle
            git
            curl
            jq
          ];

          JAVA_HOME = jdk.home;

          shellHook = ''
            export PATH="$JAVA_HOME/bin:$PATH"

            echo "Java environment ready"
            java -version
            gradle --version | head -n 3
          '';
        };
      });
}