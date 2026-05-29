import os

dirs = [
    "src/main/java/com/example/forage/repository",
    "src/main/java/com/example/forage/controller",
    "src/main/java/com/example/forage/service",
    "src/main/java/com/example/forage/dto"
]

for d in dirs:
    for root, _, files in os.walk(d):
        for f in files:
            if f.endswith(".java"):
                path = os.path.join(root, f)
                with open(path, "r") as file:
                    content = file.read()
                
                new_content = content.replace("Long", "Integer")
                if content != new_content:
                    with open(path, "w") as file:
                        file.write(new_content)
                    print(f"Updated {path}")
