#! /usr/bin/env python3
import os
import os.path

import javalang

from plantuml_composer import Root
from plantuml_composer import Class
from plantuml_composer import Package


def walk_java_files(path):
    for root, dirs, files in os.walk(path):
        for fn in files:
            if not fn.endswith(".java"):
                continue

            yield os.path.join(root, fn)


class Mapper:
    def __init__(self):
        self.packages = {}
        self.classes = []
        self.root = Root()

    def map(self, path: str):
        for path in walk_java_files(path):
            with open(path) as f:
                content = f.read()

            fn = os.path.basename(path)
            tree = javalang.parse.parse(content)

            if tree.package.name in self.packages:
                package = self.packages[tree.package.name]
            else:
                package = Package(tree.package.name)
                self.packages[tree.package.name] = package

            for cls in tree.types:
                class_item = Class(cls.name)
                if class_item not in package:
                    package.add(class_item)

        self._store_packages()

    def _store_packages(self):
        for package in self.packages.values():
            self.root.add(package)

            up_package_name = self.up_in_dot_notation(package.name)
            if up_package_name in self.packages:
                up_package = self.packages[up_package_name]

                if package not in up_package:
                    up_package.add(package)

    def up_in_dot_notation(self, path):
        tokens = path.split(".")
        if len(tokens) == 1:
            return None

        tokens.pop()
        return ".".join(tokens)


    def save_to(self, path: str):
        with open(path, "wt") as f:
            f.write(self.root.to_str())


mapper = Mapper()
mapper.map("../../")
mapper.save_to("structure.plantuml")
