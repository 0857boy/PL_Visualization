import os

def generate_tree_structure(base_dir, prefix=""):
    tree = []
    for item in sorted(os.listdir(base_dir)):
        path = os.path.join(base_dir, item)
        if os.path.isdir(path):
            tree.append(f"{prefix}├── {item}")
            tree += generate_tree_structure(path, prefix + "│   ")
        else:
            tree.append(f"{prefix}├── {item}")
    return tree

def update_readme(project_root):
    readme_path = os.path.join(project_root, "README.md")
    tree_structure = "\n".join(generate_tree_structure(project_root))
    
    with open(readme_path, "r", encoding="utf-8") as file:
        content = file.readlines()
    
    # Replace section marked for project tree
    start_marker = "<!-- PROJECT TREE START -->"
    end_marker = "<!-- PROJECT TREE END -->"
    start_idx = content.index(start_marker + "\n")
    end_idx = content.index(end_marker + "\n")
    
    updated_content = (
        content[:start_idx + 1]
        + ["\n```\n"] + [tree_structure] + ["\n```\n"]
        + content[end_idx:]
    )
    
    with open(readme_path, "w", encoding="utf-8") as file:
        file.writelines(updated_content)

if __name__ == "__main__":
    project_root = os.path.dirname(os.path.abspath(__file__))
    update_readme(project_root)
