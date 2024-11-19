import os
import difflib

def generate_tree_structure(base_dir, prefix=""):
    # 生成目錄結構
    tree = []
    items = sorted(os.listdir(base_dir))
    for item in items:
        if item.startswith('.') or item == "update_readme.py":
            continue  # 省略以 . 開頭的檔案和 update_readme.py
        path = os.path.join(base_dir, item)
        if os.path.isdir(path):
            tree.append(f"{prefix}├── {item}")
            tree += generate_tree_structure(path, prefix + "│   ")
        else:
            tree.append(f"{prefix}├── {item}")
    return tree

def read_current_structure(readme_path):
    # 讀取現有 README 中的項目結構
    with open(readme_path, "r", encoding="utf-8") as file:
        content = file.readlines()
    start_marker = "<!-- PROJECT TREE START -->"
    end_marker = "<!-- PROJECT TREE END -->"
    start_idx = content.index(start_marker + "\n")
    end_idx = content.index(end_marker + "\n")
    return [line.rstrip() for line in content[start_idx + 1 : end_idx]]

def update_readme(project_root):
    # 更新 README 並返回變更摘要
    readme_path = os.path.join(project_root, "README.md")
    tree_structure = "\n".join(generate_tree_structure(project_root))
    current_structure = "\n".join(read_current_structure(readme_path))

    # 比較現有結構與新結構
    if current_structure.strip() == tree_structure.strip():
        return None  # 沒有變化
    
    # 計算變更內容
    diff = difflib.unified_diff(
        current_structure.splitlines(),
        tree_structure.splitlines(),
        lineterm=""
    )
    diff_summary = "\n".join(diff)

    # 更新 README
    with open(readme_path, "r", encoding="utf-8") as file:
        content = file.readlines()
    start_marker = "<!-- PROJECT TREE START -->"
    end_marker = "<!-- PROJECT TREE END -->"
    start_idx = content.index(start_marker + "\n")
    end_idx = content.index(end_marker + "\n")
    updated_content = (
        content[:start_idx + 1]
        + [tree_structure] + ["\n"]
        + content[end_idx:]
    )
    with open(readme_path, "w", encoding="utf-8") as file:
        file.writelines(updated_content)

    return diff_summary  # 返回變更摘要

if __name__ == "__main__":
    project_root = os.path.dirname(os.path.abspath(__file__))
    changes = update_readme(project_root)
    if changes:
        print("README.md updated with the following changes:")
        print(changes)
    else:
        print("No changes detected.")